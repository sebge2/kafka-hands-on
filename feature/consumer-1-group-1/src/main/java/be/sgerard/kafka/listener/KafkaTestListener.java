package be.sgerard.kafka.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaTestListener {

    @KafkaListener(topics = "${kafka.topic}")
    public void listen(@Payload String message,
                       @Header(value = KafkaHeaders.MESSAGE_KEY, required = false) String key,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                       @Header(KafkaHeaders.OFFSET) int offset,
                       @Header(KafkaHeaders.GROUP_ID) String groupId) {
        System.out.println("================================");
        System.out.println("Message: " + message);
        System.out.println("Key: " + key);
        System.out.println("Group ID: " + groupId);
        System.out.println("Partition: " + partition);
        System.out.println("Offset: " + offset);
        System.out.println("================================");
        System.out.println("");
    }
}
