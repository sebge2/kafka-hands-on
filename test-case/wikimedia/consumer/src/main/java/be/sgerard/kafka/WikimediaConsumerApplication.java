package be.sgerard.kafka;

import be.sgerard.kafka.configuration.OpensearchProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({OpensearchProperties.class})
public class WikimediaConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WikimediaConsumerApplication.class, args);
    }

}
