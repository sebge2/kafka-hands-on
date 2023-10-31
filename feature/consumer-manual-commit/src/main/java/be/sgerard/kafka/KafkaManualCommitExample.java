package be.sgerard.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Map;
import java.util.Properties;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;

public class KafkaManualCommitExample {

    public static void main(String[] args) {
        final Properties properties = new Properties();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092");

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group-manual-commit-test");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties)) {
            consumer.subscribe(singletonList("test"));

            final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMinutes(1));

            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("topic %s, partition %s, offset %s, value %s", record.topic(), record.partition(), record.offset(), record.value());

                commitSync(record, consumer);
            }
        }
    }

    private static void commitSync(ConsumerRecord<String, String> record, KafkaConsumer<String, String> consumer) {
        final Map<TopicPartition, OffsetAndMetadata> offsets = singletonMap(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );

        consumer.commitSync(offsets);
    }
}
