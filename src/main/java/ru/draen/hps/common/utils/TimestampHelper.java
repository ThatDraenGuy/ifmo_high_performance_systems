package ru.draen.hps.common.utils;

import lombok.NonNull;

import java.time.*;

public class TimestampHelper {

    public static Instant current() {
        return Instant.now(); //TODO
    }

    public static Instant toInstant(@NonNull LocalDateTime moment) {
        return moment.atZone(ZoneId.systemDefault()).toInstant(); //TODO
    }

    public static Instant toInstant(@NonNull LocalDate date) {
        return toInstant(date.atStartOfDay());
    }

    public static OffsetDateTime atOffset(@NonNull Instant instant) {
        return instant.atOffset(ZoneOffset.UTC); //TODO
    }
}
