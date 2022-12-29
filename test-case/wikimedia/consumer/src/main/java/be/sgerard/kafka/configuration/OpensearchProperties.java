package be.sgerard.kafka.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "opensearch")
@Getter
@Setter
@Accessors(chain = true)
public final class OpensearchProperties {

    private String hostname;
    private Integer port;
    private String scheme;
    private String username;
    private String password;
    private boolean insecure;

    private String index;
}
