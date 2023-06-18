package be.sgerard.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toMap;

public class TransactionApplication {

    public static void main(String[] args) throws InterruptedException {
        final Properties producerConfig = new Properties();
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092,localhost:29092");
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfig.put(ProducerConfig.CLIENT_ID_CONFIG, "test-transaction");
        producerConfig.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transaction");

        final Properties consumerConfig = new Properties();
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092,localhost:29092");
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
        consumerConfig.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        System.out.println("Start producer/consumer");

        try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerConfig); KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfig)) {
            System.out.println("Start transactions");
            producer.initTransactions();

            System.out.println("Subscribe");
            consumer.subscribe(singletonList("test"));

            while (true) {
                System.out.println("Waiting for records");

                final ConsumerRecords<String, String> records = consumer.poll(Duration.of(1, ChronoUnit.MINUTES));

                System.out.println("Fetch records " + records.count());

                if (!records.isEmpty()) {
                    producer.beginTransaction();

                    records.forEach(record -> producer.send(transform(record)));


                    final Map<TopicPartition, OffsetAndMetadata> offsets = StreamSupport
                            .stream(records.spliterator(), false)
                            .map(record -> new TopicPartition(record.topic(), record.partition()))
                            .distinct()
                            .collect(toMap(
                                    Function.identity(),
                                    topicPartition -> new OffsetAndMetadata(consumer.position(topicPartition))
                            ));
                    final ConsumerGroupMetadata groupMetadata = consumer.groupMetadata();

                    producer.sendOffsetsToTransaction(offsets, groupMetadata);

                    producer.commitTransaction();
                }
            }
        }
    }

    private static ProducerRecord<String, String> transform(ConsumerRecord<String, String> record) {
        return new ProducerRecord<>("my-transaction", record.key(), Objects.toString(record.value(), "") + "-transformed");
    }
}
