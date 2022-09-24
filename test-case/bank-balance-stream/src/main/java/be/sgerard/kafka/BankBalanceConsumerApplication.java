package be.sgerard.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

import static be.sgerard.kafka.BankBalanceProducerApplication.*;

public class BankBalanceConsumerApplication {
    public static final Serde<String> STRING_SERDE = Serdes.String();
    public static final Serde<JsonNode> JSON_SERDE = Serdes.serdeFrom(new JsonSerializer(), new JsonDeserializer());

    public static void main(String[] args) {
        final Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "map-example");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, STRING_SERDE.getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, STRING_SERDE.getClass());
        properties.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE_V2);

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092,localhost:29092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        final KafkaStreams stream = new KafkaStreams(createTopology(), properties);
        stream.cleanUp();
        stream.start();

        Runtime.getRuntime().addShutdownHook(new Thread(stream::close));
    }

    private static Topology createTopology() {
        final StreamsBuilder builder = new StreamsBuilder();

        builder.stream("bank-transaction", Consumed.with(Serdes.Void(), JSON_SERDE))
                .selectKey((k, v) -> v.get(NAME).asText())
                .groupByKey(Grouped.with(STRING_SERDE, JSON_SERDE))
                .aggregate(
                        BankBalanceConsumerApplication::createEmpty,
                        (key, value, aggregate) -> {
                            final ObjectNode balance = JsonNodeFactory.instance.objectNode();

                            balance.put(NAME, key);
                            balance.put(AMOUNT, combineAmount(value.get(AMOUNT), aggregate.get(AMOUNT)));
                            balance.put(TIME, combineTimestamp(value.get(TIME), aggregate.get(TIME)));

                            return balance;
                        },
                        Named.as("bank-Transaction-Aggregate"),
                        Materialized.with(STRING_SERDE, JSON_SERDE)
                )
                .toStream()
                .to("bank-balance", Produced.with(STRING_SERDE, JSON_SERDE));

        return builder.build();
    }

    private static JsonNode createEmpty() {
        final ObjectNode balance = JsonNodeFactory.instance.objectNode();

        balance.put(AMOUNT, 0);

        return balance;
    }

    private static int combineAmount(JsonNode... values) {
        return Stream.of(values)
                .filter(Objects::nonNull)
                .filter(node -> !node.isNull())
                .map(JsonNode::asInt)
                .reduce(Integer::sum)
                .orElse(0);
    }

    private static String combineTimestamp(JsonNode... values) {
        return Stream.of(values)
                .filter(Objects::nonNull)
                .filter(node -> !node.isNull())
                .map(JsonNode::asText)
                .map(Instant::parse)
                .max(Instant::compareTo)
                .map(Instant::toString)
                .orElse(null);
    }
}
