package be.sgerard.kafka.configuration;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;

import static be.sgerard.kafka.configuration.KafkaConfiguration.STRING_SERDE;

@Configuration
public class TopologyConfiguration {

    @Value(value = "${kafka.topic-in}")
    private String topicInput;

    @Value(value = "${kafka.topic-out}")
    private String topicOutput;

    @Autowired
    private StreamsBuilder streamsBuilder;

    @PostConstruct
    void buildPipeline() {
        final KStream<String, String> messageStream = streamsBuilder
                .stream(topicInput, Consumed.with(STRING_SERDE, STRING_SERDE));

        final KTable<String, Long> wordCounts = messageStream
                .mapValues((ValueMapper<String, String>) String::toLowerCase)
                .flatMapValues(value -> Arrays.asList(value.split("\\W+")))
                .groupBy((key, word) -> word, Grouped.with(STRING_SERDE, STRING_SERDE))
                .count();

        wordCounts.toStream().to(topicOutput);
    }
}
