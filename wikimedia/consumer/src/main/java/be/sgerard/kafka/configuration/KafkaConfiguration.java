package be.sgerard.kafka.configuration;

import be.sgerard.kafka.model.dto.WikimediaEventDto;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfiguration {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${kafka.groupId}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, WikimediaEventDto> consumerFactory() {
        final Map<String, Object> properties = new HashMap<>();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, "200");
        properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "10");

        var valueDeserializer = new JsonDeserializer<>(WikimediaEventDto.class);
        valueDeserializer.typeResolver((topic, data, headers) -> SimpleType.constructUnsafe(WikimediaEventDto.class));

        return new DefaultKafkaConsumerFactory<>(properties, new StringDeserializer(), valueDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WikimediaEventDto> kafkaListenerContainerFactory() {
        final ConcurrentKafkaListenerContainerFactory<String, WikimediaEventDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        // batch
        factory.setBatchListener(true);

        return factory;
    }
}
