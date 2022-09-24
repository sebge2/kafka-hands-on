package be.sgerard.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProducerAvroApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerAvroApplication.class, args);
    }
}
