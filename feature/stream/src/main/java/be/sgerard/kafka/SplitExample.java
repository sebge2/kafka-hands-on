package be.sgerard.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class SplitExample {
    public static final Serde<String> STRING_SERDE = Serdes.String();

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

        final Map<String, KStream<String, String>> wordCounts = builder.
                <String, String>stream("word-count-input")
                .split()
                .branch((key, value) -> Objects.equals(value, "Hello World"),
                        Branched.withConsumer(ks -> ks.to("stream-example-split-hello"))
                )
                .branch((key, value) -> Objects.equals(value, "totoz"),
                        Branched.withConsumer(ks -> ks.to("stream-example-split-totoz"))
                )
                .defaultBranch(
                        Branched.withConsumer(ks -> ks.to("stream-example-split-default"))
                );

        return builder.build();
    }
}
