package be.sgerard.kafka.listener;

import be.sgerard.kafka.service.WikimediaRecordService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class WikimediatListener {

    private final WikimediaRecordService recordService;

    @KafkaListener(topics = "${kafka.topic}")
    public void listen(@Payload String message) throws IOException {
        recordService.record(message);
    }
}
