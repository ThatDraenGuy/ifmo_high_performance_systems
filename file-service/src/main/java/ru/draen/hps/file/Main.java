package ru.draen.hps.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication
@ComponentScan("ru.draen.hps")
@EntityScan("ru.draen.hps.common.dbms.domain")
@EnableReactiveFeignClients("ru.draen.hps")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}