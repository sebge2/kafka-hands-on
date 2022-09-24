package be.sgerard.kafka.producer;

import be.sgerard.kafka.model.dto.external.WikimediaEventDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WikimediaListener {

    private static final String URL = "https://stream.wikimedia.org/v2/stream/recentchange";

    private final WikimediaSender sender;

    public WikimediaListener(WikimediaSender sender) {
        this.sender = sender;
    }

    public void listen() {
        WebClient
                .create(URL)
                .get()
                .retrieve()
                .bodyToFlux(WikimediaEventDto.class)
                .subscribe(sender::send);
    }

}
