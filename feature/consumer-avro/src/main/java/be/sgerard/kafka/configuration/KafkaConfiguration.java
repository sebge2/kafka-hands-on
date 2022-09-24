package be.sgerard.kafka.configuration;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import be.sgerard.kafkahandson.Customer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfiguration {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${kafka.groupIdSpring}")
    private String groupIdSpring;


    @Value(value = "${kafka.groupIdNative}")
    private String groupIdNative;

    @Value(value = "${kafka.schema-registry.url}")
    private String schemaRegistryUrl;

    @Bean
    public ConsumerFactory<String, Customer> consumerFactorySpring() {
        final Map<String, Object> properties = new HashMap<>();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupIdSpring);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        properties.put("schema.registry.url", schemaRegistryUrl);
        properties.put("specific.avro.reader", "true"); // otherwise messages will be mapped to a generic record

        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    public ConsumerFactory<String, Customer> consumerFactoryNative() {
        final Map<String, Object> properties = new HashMap<>();

        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupIdNative);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        properties.put("schema.registry.url", schemaRegistryUrl);
        properties.put("specific.avro.reader", "true"); // otherwise messages will be mapped to a generic record

        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Customer> kafkaListenerContainerFactory() {
        final ConcurrentKafkaListenerContainerFactory<String, Customer> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactorySpring());

        return factory;
    }
}
