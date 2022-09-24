package be.sgerard.kafka.sender;

import be.sgerard.kafkahandson.Customer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaTestSender {

    private final String topic;
    private final KafkaProducer<String, Customer> producer; // native API
    private final KafkaTemplate<String, Customer> template; // Spring API

    public KafkaTestSender(@Value("${kafka.topic}") String topic,
                           KafkaTemplate<String, Customer> template,
                           ProducerFactory<String, Customer> producerFactory) {
        this.topic = topic;
        this.producer = new KafkaProducer<>(producerFactory.getConfigurationProperties());
        this.template = template;
    }

    @Scheduled(fixedDelay = 15000, initialDelay = 1000)
    public void sendMessage() throws ExecutionException, InterruptedException {
        System.out.println("Send message with Avro on topic " + topic);

        for (int i = 0; i < 10; i++) {
            final Customer customer = Customer.newBuilder()
                    .setFirstName(UUID.randomUUID().toString())
                    .setLastName(UUID.randomUUID().toString())
                    .build();

            final String key = "key-%s".formatted(i);

            sendWithNativeApi(key, customer);
            sendWithSpringApi(key, customer);
        }
    }

    private void sendWithNativeApi(String key, Customer customer) {
        final ProducerRecord<String, Customer> producerRecord = new ProducerRecord<>(topic, key, customer);

        producer.send(producerRecord, (recordMetadata, e) -> {
            System.out.println("================================");
            System.out.println("Message with Native API: " + customer);
            System.out.println("Key: " + key);
            System.out.println("Partition: " + recordMetadata.partition());
            System.out.println("Offset: " + recordMetadata.offset());
            System.out.println("================================");
            System.out.println();
        });
    }

    private void sendWithSpringApi(String key, Customer customer) throws InterruptedException, ExecutionException {
        final SendResult<String, Customer> sendResult = template.send(topic, key, customer).completable().get();

        System.out.println("================================");
        System.out.println("Message with Spring API: " + sendResult.getProducerRecord().value());
        System.out.println("Key: " + sendResult.getProducerRecord().key());
        System.out.println("Partition: " + sendResult.getRecordMetadata().partition());
        System.out.println("Offset: " + sendResult.getRecordMetadata().offset());
        System.out.println("================================");
        System.out.println();
    }

}
