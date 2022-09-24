package be.sgerard.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

import static java.util.Arrays.asList;

public class JoinExample {

    public static final Serde<String> STRING_SERDE = Serdes.String();

    public static void main(String[] args) {
        final Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "bad-words-example");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092,localhost:29092");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, STRING_SERDE.getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, STRING_SERDE.getClass());

        final KafkaStreams stream = new KafkaStreams(createTopology(), properties);
        stream.cleanUp();
        stream.start();

        Runtime.getRuntime().addShutdownHook(new Thread(stream::close));
    }

    private static Topology createTopology() {
        final StreamsBuilder builder = new StreamsBuilder();

        final GlobalKTable<String, String> badWords = builder.globalTable("bad-words");

        final KStream<String, String> words = builder.
                <String, String>stream("word-count-input")
                .flatMapValues(value -> asList(value.split("\\W+")))
                .mapValues((k, v) -> v.toLowerCase())
                .selectKey((k, v) -> v);

        words.join(
                        badWords,
                        (k, v) -> k,
                        (word, badWord) -> word

                )
                .to("bad-words-detected");

        return builder.build();
    }
}
