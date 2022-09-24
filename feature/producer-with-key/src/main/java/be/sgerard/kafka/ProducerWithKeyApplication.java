package be.sgerard.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProducerWithKeyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerWithKeyApplication.class, args);
    }

}
