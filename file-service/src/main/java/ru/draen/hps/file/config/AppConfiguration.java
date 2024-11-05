package ru.draen.hps.file.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan("ru.draen.hps.common.dbms.domain")
public class AppConfiguration {
}
