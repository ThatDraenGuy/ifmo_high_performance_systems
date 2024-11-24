package ru.draen.hps.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@ComponentScan(value = "ru.draen.hps")
@EnableKafka
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
