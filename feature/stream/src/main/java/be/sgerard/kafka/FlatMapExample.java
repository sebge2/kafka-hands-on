package be.sgerard.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class FlatMapExample {
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

        final KStream<String, String> wordCounts = builder.
                <Object, String>stream("word-count-input")
                .flatMap((key, value) ->
                        Stream.of(value.split("\\W+"))
                                .map(word -> new KeyValue<>(UUID.randomUUID().toString(), word))
                                .toList()
                ); // stateless, re-partitioning;

        wordCounts.to("stream-example-flatmap");

        return builder.build();
    }
}
