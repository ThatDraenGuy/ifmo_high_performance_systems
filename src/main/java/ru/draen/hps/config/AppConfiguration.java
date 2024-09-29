package ru.draen.hps.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Getter
@Component
public class AppConfiguration {
    private ZoneId zone;
}
