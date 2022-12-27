package be.sgerard.kafka.listener;

import be.sgerard.kafka.model.dto.WikimediaEventDto;
import be.sgerard.kafka.service.WikimediaRecordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@AllArgsConstructor
public class WikimediatListener {

    private final WikimediaRecordService recordService;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @KafkaListener(topics = "${kafka.topic}")
    public void listen(@Payload List<String> messages) throws IOException {
        recordService.record(
                messages.stream()
                        .map(this::mapDto)
                        .collect(toList())
        );
    }

    private WikimediaEventDto mapDto(String event) {
        try {
            return objectMapper.readValue(event, WikimediaEventDto.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Error while deserializing event.", e);
        }
    }
}
