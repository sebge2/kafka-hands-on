package be.sgerard.kafka.model;

import be.sgerard.kafka.validator.BatchSizeValidator;
import be.sgerard.kafka.validator.TimestampValidator;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.types.Password;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static be.sgerard.kafka.utils.TimeUtils.nowPlusYear;
import static be.sgerard.kafka.utils.TimeUtils.toInstant;

/**
 * GitHub {@link AbstractConfig connector configuration}.
 */
public class GitHubSourceConnectorConfig extends AbstractConfig {

    /**
     * Kafka topic to write to.
     */
    public static final String TOPIC = "topic";

    /**
     * Owner of the GitHub repository you want to follow.
     */
    public static final String GITHUB_OWNER = "github.owner";

    /**
     * The GitHub repository you want to follow.
     */
    public static final String GITHUB_REPOSITORY = "github.repository";

    /**
     * GitHub Username to authenticate calls.
     */
    public static final String GITHUB_AUTHENTICATION_USERNAME = "github.auth.username";

    /**
     * GitHub Password to authenticate calls.
     */
    public static final String GITHUB_AUTHENTICATION_PASSWORD = "github.auth.password";

    /**
     * URL of GitHub.
     */
    public static final String GITHUB_URL = "github.url";

    /**
     * Issues updated at or after this time are returned.
     */
    public static final String SINCE_CONFIG = "since.timestamp";

    /**
     * Number of data points to retrieve at a time.
     */
    public static final String BATCH_SIZE_CONFIG = "batch.size";

    /**
     * Default value for the {@link #BATCH_SIZE_CONFIG batch size}.
     */
    public static final int DEFAULT_BATCH_SIZE_VALUE = 100;

    /**
     * Returns the {@link ConfigDef configuration} of this connector.
     */
    public static ConfigDef conf() {
        return new ConfigDef()
                .define(TOPIC, Type.STRING, Importance.HIGH, "Kafka topic to write to.")

                .define(GITHUB_OWNER, Type.STRING, Importance.HIGH, "Owner of the GitHub repository you want to follow.")
                .define(GITHUB_REPOSITORY, Type.STRING, Importance.HIGH, "The GitHub repository you want to follow.")
                .define(GITHUB_AUTHENTICATION_USERNAME, Type.STRING, null, Importance.HIGH, "Optional GitHub Username to authenticate calls.")
                .define(GITHUB_AUTHENTICATION_PASSWORD, Type.PASSWORD, null, Importance.HIGH, "Optional GitHub Password to authenticate calls.")
                .define(GITHUB_URL, Type.STRING, "https://api.github.com", Importance.LOW, "Optional URL of GitHub.")

                .define(SINCE_CONFIG, Type.STRING, nowPlusYear(-1).toString(), new TimestampValidator(), Importance.HIGH, "Only issues updated at or after this time are returned.\nThis is a timestamp in ISO 8601 format: YYYY-MM-DDTHH:MM:SSZ.\nDefaults to a year from first launch.")

                .define(BATCH_SIZE_CONFIG, Type.INT, DEFAULT_BATCH_SIZE_VALUE, new BatchSizeValidator(), Importance.LOW, "Optional number of data points to retrieve at a time. Defaults to 100 (max value)");
    }

    public GitHubSourceConnectorConfig(ConfigDef config, Map<String, String> parsedConfig) {
        super(config, parsedConfig);
    }

    public GitHubSourceConnectorConfig(Map<String, String> parsedConfig) {
        this(conf(), parsedConfig);
    }

    /**
     * @see #TOPIC
     */
    public String getTopic() {
        return this.getString(TOPIC);
    }

    /**
     * @see #GITHUB_OWNER
     */
    public String getGithubOwner() {
        return this.getString(GITHUB_OWNER);
    }

    /**
     * @see #GITHUB_REPOSITORY
     */
    public String getGithubRepository() {
        return this.getString(GITHUB_REPOSITORY);
    }

    /**
     * @see #GITHUB_URL
     */
    public String getGithubUrl() {
        return this.getString(GITHUB_URL);
    }

    /**
     * @see #GITHUB_AUTHENTICATION_USERNAME
     */
    public String getAuthenticationUsername() {
        return this.getString(GITHUB_AUTHENTICATION_USERNAME);
    }

    /**
     * @see #GITHUB_AUTHENTICATION_PASSWORD
     */
    public String getAuthenticationPassword() {
        return Optional.ofNullable(this.getPassword(GITHUB_AUTHENTICATION_PASSWORD))
                .map(Password::value)
                .orElse(null);
    }

    /**
     * @see #SINCE_CONFIG
     */
    public Instant getSince() {
        return toInstant(this.getString(SINCE_CONFIG));
    }

    /**
     * @see #BATCH_SIZE_CONFIG
     */
    public Integer getBatchSize() {
        return this.getInt(BATCH_SIZE_CONFIG);
    }

    /**
     * Returns the URL to use to retrieve issues from GitHub.
     */
    public String buildGitHubIssuesUrl(Integer page, Instant since) {
        return String.format(
                "%s/repos/%s/%s/issues?page=%s&per_page=%s&since=%s&state=all&direction=asc&sort=updated",
                getGithubUrl(),
                getGithubOwner(),
                getGithubRepository(),
                page,
                getBatchSize(),
                since.toString()
        );
    }
}
