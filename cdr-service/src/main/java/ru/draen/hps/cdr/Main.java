package ru.draen.hps.cdr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication
@EnableReactiveFeignClients
@EnableR2dbcRepositories
@EnableWebFlux
@ComponentScan("ru.draen.hps")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
