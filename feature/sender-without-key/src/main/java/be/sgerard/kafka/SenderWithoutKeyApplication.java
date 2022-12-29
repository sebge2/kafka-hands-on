package be.sgerard.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SenderWithoutKeyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SenderWithoutKeyApplication.class, args);
    }

}
