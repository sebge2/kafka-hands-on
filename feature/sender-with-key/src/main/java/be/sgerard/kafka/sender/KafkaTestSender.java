package be.sgerard.kafka.sender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaTestSender {

    private final String topic;
    private final KafkaTemplate<String, String> template;

    public KafkaTestSender(@Value("${kafka.topic}") String topic,
                           KafkaTemplate<String, String> template) {
        this.topic = topic;
        this.template = template;
    }

    @Scheduled(fixedDelay = 15000, initialDelay = 1000)
    public void sendMessage() throws ExecutionException, InterruptedException {
        System.out.println("Send message without key on topic " + topic);

        for (int i = 0; i < 10; i++) {
            final SendResult<String, String> sendResult = template.send(topic, "key-%s".formatted(i), "Hello World! " + UUID.randomUUID()).completable().get();

            System.out.println("================================");
            System.out.println("Message: " + sendResult.getProducerRecord().value());
            System.out.println("Key: " + sendResult.getProducerRecord().key());
            System.out.println("Partition: " + sendResult.getRecordMetadata().partition());
            System.out.println("Offset: " + sendResult.getRecordMetadata().offset());
            System.out.println("================================");
            System.out.println("");
        }
    }

}
