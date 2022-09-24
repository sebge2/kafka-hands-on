package be.sgerard.kafka;

import org.apache.kafka.streams.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static be.sgerard.kafka.MapValuesExample.STRING_SERDE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestingExampleTest {

    private TestInputTopic<String, String> inputTopic;
    private TestOutputTopic<String, String> outputTopic;

    @BeforeEach
    void setup() {
        final TopologyTestDriver testDriver = new TopologyTestDriver(MapValuesExample.createTopology(), initProperties());

        inputTopic = testDriver.createInputTopic("word-count-input", STRING_SERDE.serializer(), STRING_SERDE.serializer());
        outputTopic = testDriver.createOutputTopic("stream-example-map-values", STRING_SERDE.deserializer(), STRING_SERDE.deserializer());
    }

    @Test
    void threeWords() {
        inputTopic.pipeInput("first second third");

        assertEquals(outputTopic.readKeyValue(), new KeyValue<>(null, "FIRST SECOND THIRD"));
    }

    private Properties initProperties() {
        final Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "map-values-example");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092,localhost:29092");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, STRING_SERDE.getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, STRING_SERDE.getClass());

        return config;
    }
}
