package be.sgerard.kafka.listener;

import be.sgerard.kafkahandson.Customer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static java.util.Collections.singletonList;

@Component
public class KafkaNativeListener implements ApplicationRunner {

    private final ConsumerFactory<String, Customer> consumerFactory;
    private final String topic;

    public KafkaNativeListener(@Qualifier("consumerFactoryNative") ConsumerFactory<String, Customer> consumerFactory,
                               @Value("${kafka.topic}") String topic) {
        this.consumerFactory = consumerFactory;
        this.topic = topic;
    }

    @Override
    public void run(ApplicationArguments args) {
        try (KafkaConsumer<String, Customer> consumer = new KafkaConsumer<>(consumerFactory.getConfigurationProperties())) {
            consumer.subscribe(singletonList(topic));

            while (true) {
                final ConsumerRecords<String, Customer> records = consumer.poll(500);

                records.forEach(record -> {
                    System.out.println("================================");
                    System.out.println("Message consumed with Native API: " + record.value());
                    System.out.println("Key: " + record.key());
                    System.out.println("Partition: " + record.partition());
                    System.out.println("Offset: " + record.offset());
                    System.out.println("================================");
                    System.out.println();
                });
            }
        }
    }
}
