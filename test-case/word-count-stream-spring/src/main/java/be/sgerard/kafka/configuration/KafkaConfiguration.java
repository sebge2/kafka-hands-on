package be.sgerard.kafka.configuration;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.streams.StreamsConfig.*;

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaConfiguration {

    // from https://www.baeldung.com/spring-boot-kafka-streams

    public static final Serde<String> STRING_SERDE = Serdes.String();

    @Value(value = "${kafka.bootstrapServers}")
    private String bootstrapAddress;

    @Value(value = "${kafka.app-name}")
    private String appName;

    @Bean
    KafkaStreamsConfiguration defaultKafkaStreamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(APPLICATION_ID_CONFIG, appName);
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, STRING_SERDE.getClass().getName());
        props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, STRING_SERDE.getClass().getName());

        return new KafkaStreamsConfiguration(props);
    }
}
