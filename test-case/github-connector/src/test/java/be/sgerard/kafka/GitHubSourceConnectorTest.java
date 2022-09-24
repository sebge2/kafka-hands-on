package be.sgerard.kafka;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static be.sgerard.kafka.model.GitHubSourceConnectorConfig.*;
import static org.junit.Assert.assertEquals;

public class GitHubSourceConnectorTest {

    @Test
    public void taskConfigsShouldReturnOneTaskConfig() {
        final GitHubSourceConnector gitHubSourceConnector = new GitHubSourceConnector();

        gitHubSourceConnector.start(initialConfig());

        assertEquals(gitHubSourceConnector.taskConfigs(1).size(), 1);
        assertEquals(gitHubSourceConnector.taskConfigs(10).size(), 1);
    }

    private Map<String, String> initialConfig() {
        final Map<String, String> props = new HashMap<>();

        props.put(GITHUB_OWNER, "foo");
        props.put(GITHUB_REPOSITORY, "bar");
        props.put(SINCE_CONFIG, "2017-04-26T01:23:45Z");
        props.put(BATCH_SIZE_CONFIG, "100");
        props.put(TOPIC, "github-issues");

        return (props);
    }
}
