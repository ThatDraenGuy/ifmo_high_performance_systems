package ru.draen.hps.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ru.draen.hps")
@EntityScan("ru.draen.hps.common.dbms.domain")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}