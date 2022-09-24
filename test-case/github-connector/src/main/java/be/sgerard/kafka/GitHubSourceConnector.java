package be.sgerard.kafka;

import java.util.List;
import java.util.Map;

import be.sgerard.kafka.model.GitHubSourceConnectorConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;

import static java.util.Collections.singletonList;

/**
 * {@link SourceConnector Connector} retrieving GitHub issues.
 */
public class GitHubSourceConnector extends SourceConnector {

    private GitHubSourceConnectorConfig config;

    @Override
    public String version() {
        return VersionUtil.getVersion();
    }

    @Override
    public void start(Map<String, String> map) {
        config = new GitHubSourceConnectorConfig(map);
    }

    @Override
    public Class<? extends Task> taskClass() {
        return GitHubSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int i) {
        return singletonList(config.originalsStrings()); // only one task allowed
    }

    @Override
    public void stop() {
    }

    @Override
    public ConfigDef config() {
        return GitHubSourceConnectorConfig.conf();
    }
}
