package be.sgerard.kafka.producer;

import be.sgerard.kafka.model.dto.external.WikimediaEventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class WikimediaSender {

    private static final Logger logger = LoggerFactory.getLogger(WikimediaSender.class);
    private final KafkaTemplate<String, Object> sender;
    private final String topic;

    public WikimediaSender(KafkaTemplate<String, Object> sender,
                           @Value("${kafka.topic}") String topic) {
        this.sender = sender;
        this.topic = topic;
    }

    public void send(WikimediaEventDto data) {
        logger.info("Send data");

        final var payload = new be.sgerard.kafka.model.dto.api.WikimediaEventDto(data.meta().id(), data.title(), data.user(), data.bot(), data.serverName(), data.comment());

        sender.send(topic, payload);
    }
}
