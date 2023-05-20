package be.sgerard.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.KafkaAdminClient;

import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class AdminApplication {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:19092,localhost:29092");

        try (KafkaAdminClient client = (KafkaAdminClient) KafkaAdminClient.create(config)) {
            final Set<String> topics = client.listTopics().names().get();
            System.out.println("Topics: " + topics);
        }
    }

}
