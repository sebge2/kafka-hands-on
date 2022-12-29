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

    private final String topic1;
    private final String topic2;
    private final KafkaTemplate<String, String> template;

    public KafkaTestSender(@Value("${kafka.topic-1}") String topic1,
                           @Value("${kafka.topic-2}") String topic2,
                           KafkaTemplate<String, String> template) {
        this.topic1 = topic1;
        this.topic2 = topic2;
        this.template = template;
    }

    @Scheduled(fixedDelay = 10000, initialDelay = 0)
    public void sendMessageTopic1() throws ExecutionException, InterruptedException {
        System.out.println("Send message without key on topic " + topic1);

        final SendResult<String, String> sendResult = template.send(topic1, "Hello World! " + UUID.randomUUID()).completable().get();

        System.out.println("================================");
        System.out.println("Message: " + sendResult.getProducerRecord().value());
        System.out.println("Key: " + sendResult.getProducerRecord().key());
        System.out.println("Partition: " + sendResult.getRecordMetadata().partition());
        System.out.println("Offset: " + sendResult.getRecordMetadata().offset());
        System.out.println("================================");
        System.out.println("");
    }

    @Scheduled(fixedDelay = 20000, initialDelay = 10000)
    public void sendMessageTopic2() throws ExecutionException, InterruptedException {
        System.out.println("Send message without key on topic " + topic2);

        final SendResult<String, String> sendResult = template.send(topic2, "Hello World! " + UUID.randomUUID()).completable().get();

        System.out.println("================================");
        System.out.println("Message: " + sendResult.getProducerRecord().value());
        System.out.println("Key: " + sendResult.getProducerRecord().key());
        System.out.println("Partition: " + sendResult.getRecordMetadata().partition());
        System.out.println("Offset: " + sendResult.getRecordMetadata().offset());
        System.out.println("================================");
        System.out.println("");
    }

}
