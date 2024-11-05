package ru.draen.hps.billing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableFeignClients
@EnableR2dbcRepositories
@ComponentScan("ru.draen.hps")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
