package be.sgerard.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

public class SerdesExample {

    public static final Serde<String> STRING_SERDE = Serdes.String();
    public static final Serde<Void> VOID_SERDE = Serdes.Void();

    public static void main(String[] args) {
        final Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "map-example");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092,localhost:29092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, STRING_SERDE.getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, STRING_SERDE.getClass());

        try (KafkaStreams streams = new KafkaStreams(createTopology(), config)) {
            streams.start();

            while (true) {
                streams.metadataForLocalThreads()
                        .forEach(System.out::println);

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    private static Topology createTopology() {
        final StreamsBuilder builder = new StreamsBuilder();

        final KStream<String, String> wordCounts = builder
                .stream("word-count-input", Consumed.with(VOID_SERDE, STRING_SERDE))
                .selectKey((key, value) -> value.isEmpty() ? "" : value.substring(0, 1)); // stateless, re-partitioning;

        wordCounts.to("stream-example-serdes");

        return builder.build();
    }
}
