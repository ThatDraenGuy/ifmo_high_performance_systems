package ru.draen.hps.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(value = "ru.draen.hps", excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = ru.draen.hps.common.webmvc.service.AppUserDetailsService.class))
@EntityScan("ru.draen.hps.common.dbms.domain")
@EnableFeignClients("ru.draen.hps")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}