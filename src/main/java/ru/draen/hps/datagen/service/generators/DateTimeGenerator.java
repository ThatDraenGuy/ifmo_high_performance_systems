package ru.draen.hps.datagen.service.generators;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@Component
public class DateTimeGenerator implements IGenerator<Period, LocalDateTime> {
    private final Random random = new Random();

    @Override
    public LocalDateTime generate(Period period) {
        long duration = Duration.between(period.from(), period.to()).getSeconds();
        long offset = random.nextLong(0, duration);
        return period.from().plusSeconds(offset);
    }

}
