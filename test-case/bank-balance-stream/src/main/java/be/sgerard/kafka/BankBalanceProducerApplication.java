package be.sgerard.kafka;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BankBalanceProducerApplication {

    public static final String NAME = "name";
    public static final String AMOUNT = "amount";
    public static final String TIME = "time";

    public static void main(String[] args) {
        final Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092,localhost:29092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        generate(properties);
    }

    private static void generate(Map<String, Object> properties) {
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            long index = 0;

            while (true) {
                producer.send(new ProducerRecord<>("bank-transaction", generateJson(index++)));

                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static String generateJson(long index) {
        return generate(index).toString();
    }

    private static ObjectNode generate(long index) {
        final List<String> firstNames = getFirstNames();

        final ObjectNode node = JsonNodeFactory.instance.objectNode();

        node.put(NAME, firstNames.get((int) index % firstNames.size()));
        node.put(AMOUNT, 300 + new Random().nextInt(100));
        node.put(TIME, Instant.now().toString());

        return node;
    }

    private static List<String> getFirstNames() {
        try {
            return Files.readAllLines(Path.of(BankBalanceConsumerApplication.class.getResource("/firstName.txt").getPath()));
        } catch (IOException e) {
            throw new RuntimeException("Error while retrieving first names.", e);
        }
    }
}
