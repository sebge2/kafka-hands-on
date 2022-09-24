package be.sgerard.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsumerAvroApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerAvroApplication.class, args);
    }
}
