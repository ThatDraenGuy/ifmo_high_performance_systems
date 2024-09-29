package ru.draen.hps.datagen.service.generators;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PhoneNumberGeneratorImpl implements IGenerator<Void, String> {
    private final Random random = new Random();

    @Override
    public String generate(Void unused) {
        long number = random.nextLong(1000000000L, 9999999999L);
        return "7" + number;
    }
}
