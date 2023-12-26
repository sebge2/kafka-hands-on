package be.sgerard.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Reducer;

import java.util.Properties;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class ReduceExample {

    public static final Serde<String> STRING_SERDE = Serdes.String();
    public static final Serde<Long> LONG_SERDE = Serdes.Long();

    public static void main(String[] args) {
        final Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "reduce-example");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092,localhost:29092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, STRING_SERDE.getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, LONG_SERDE.getClass());

        try (KafkaStreams streams = new KafkaStreams(createTopology(), config)) {
            streams.cleanUp();

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

        final KTable<String, Long> wordCounts = builder
                .stream("word-count-input", Consumed.with(Serdes.Bytes(), STRING_SERDE))
                .flatMapValues(words ->
                        Stream.of(words.split("\\W+"))
                                .filter(word -> !word.isEmpty())
                                .toList()
                )
                .selectKey((k, v) -> v)
                .mapValues((k, v) -> 1L)
                .groupByKey()
                .reduce(Long::sum); // counts the number of time a word appears

        wordCounts.toStream().to("stream-example-reduce", Produced.with(STRING_SERDE, LONG_SERDE));

        return builder.build();
    }
}
