package be.sgerard.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.ProcessorSupplier;
import org.apache.kafka.streams.processor.api.Record;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

import java.time.Duration;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.singleton;

public class ProcessorApiExample {

    private static final String STORE_NAME = "myStore";

    public static void main(String[] args) {
        final Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "processor-api-example");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092,localhost:29092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        final Topology topology = new Topology()
                .addSource(
                        "source-node",
                        Serdes.Void().deserializer(),
                        Serdes.String().deserializer(),
                        "word-count-input"
                )
                .addProcessor(
                        "counter",
                        new ProcessorSupplier<Void, String, String, Long>() {
                            @Override
                            public Processor<Void, String, String, Long> get() {
                                return new Counter();
                            }

                            @Override
                            public Set<StoreBuilder<?>> stores() {
                                return singleton(
                                        Stores.keyValueStoreBuilder(
                                                Stores.persistentKeyValueStore(STORE_NAME),
                                                Serdes.String(),
                                                Serdes.Long()
                                        )
                                );
                            }
                        },
                        "source-node"
                )
                .addSink(
                        "sink-node",
                        "word-count-processor-api",
                        Serdes.String().serializer(),
                        Serdes.Long().serializer(),
                        "counter"
                );

        try (KafkaStreams streams = new KafkaStreams(topology, config)) {
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

    /**
     * The processor receives records from the input topic and produces records for the output topic.
     */
    private static class Counter implements Processor<Void, String, String, Long> {

        private ProcessorContext<String, Long> context;
        private KeyValueStore<String, Long> store;

        @Override
        public void init(ProcessorContext<String, Long> context) {
            Processor.super.init(context);

            this.context = context;
            store = this.context.getStateStore(STORE_NAME);

            this.context.schedule(Duration.ofSeconds(30), PunctuationType.STREAM_TIME, this::forwardAll);
        }

        @Override
        public void process(Record<Void, String> record) {
            System.out.printf("Process record %s %s%n", record.key(), record.value());

            store.put(
                    record.value(),
                    Optional.ofNullable(record.value()).stream()
                            .flatMap(value -> Stream.of(value.split(" ")))
                            .map(value -> (long) value.length())
                            .reduce(0L, Long::sum)
            );
        }

        /**
         * Forwards records that have been processed. If there are not forwarded, they won't end up in the output topic.
         */
        private void forwardAll(long timestamp) {
            try (KeyValueIterator<String, Long> iterator = store.all()) {
                while (iterator.hasNext()) {
                    final KeyValue<String, Long> record = iterator.next();

                    context.forward(new Record<>(record.key, record.value, timestamp));
                }
            }
        }
    }
}
