package be.sgerard.kafka;

import be.sgerard.kafka.model.GitHubSourceConnectorConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigValue;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static be.sgerard.kafka.model.GitHubSourceConnectorConfig.*;

public class GitHubSourceConnectorConfigTest {

    private final ConfigDef configDef = GitHubSourceConnectorConfig.conf();

    @Test
    public void initialConfigIsValid() {
        assert (configDef.validate(initialConfig())
                .stream()
                .allMatch(configValue -> configValue.errorMessages().size() == 0));
    }

    @Test
    public void canReadConfigCorrectly() {
        final GitHubSourceConnectorConfig config = new GitHubSourceConnectorConfig(initialConfig());

        config.getAuthenticationPassword();
    }

    @Test
    public void validateSince() {
        final Map<String, String> config = initialConfig();
        config.put(SINCE_CONFIG, "not-a-date");

        final ConfigValue configValue = configDef.validateAll(config).get(SINCE_CONFIG);

        assert (configValue.errorMessages().size() > 0);
    }

    @Test
    public void validateBatchSizeNegative() {
        final Map<String, String> config = initialConfig();
        config.put(BATCH_SIZE_CONFIG, "-1");

        final ConfigValue configValue = configDef.validateAll(config).get(BATCH_SIZE_CONFIG);

        assert (configValue.errorMessages().size() > 0);
    }

    @Test
    public void validateBatchSizeTooHigh() {
        final Map<String, String> config = initialConfig();
        config.put(BATCH_SIZE_CONFIG, "101");

        final ConfigValue configValue = configDef.validateAll(config).get(BATCH_SIZE_CONFIG);

        assert (configValue.errorMessages().size() > 0);
    }

    @Test
    public void validateUsername() {
        final Map<String, String> config = initialConfig();
        config.put(GITHUB_AUTHENTICATION_USERNAME, "username");

        final ConfigValue configValue = configDef.validateAll(config).get(GITHUB_AUTHENTICATION_USERNAME);

        assert (configValue.errorMessages().size() == 0);
    }

    @Test
    public void validatePassword() {
        final Map<String, String> config = initialConfig();
        config.put(GITHUB_AUTHENTICATION_PASSWORD, "password");

        final ConfigValue configValue = configDef.validateAll(config).get(GITHUB_AUTHENTICATION_PASSWORD);

        assert (configValue.errorMessages().size() == 0);
    }

    private Map<String, String> initialConfig() {
        final Map<String, String> baseProps = new HashMap<>();
        baseProps.put(GITHUB_OWNER, "foo");
        baseProps.put(GITHUB_REPOSITORY, "bar");
        baseProps.put(SINCE_CONFIG, "2017-04-26T01:23:45Z");
        baseProps.put(BATCH_SIZE_CONFIG, "100");
        baseProps.put(TOPIC, "github-issues");

        return baseProps;
    }
}