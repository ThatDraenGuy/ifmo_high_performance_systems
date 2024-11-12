package ru.draen.hps.common.core.utils;

import jakarta.annotation.Nullable;

import java.time.*;

import static java.util.Objects.isNull;

public class TimestampHelper {
    public static final LocalDateTime MAX_DATE_TIME = LocalDateTime.of(
            2999, 12, 31, 23, 59, 59
    );
    public static final Instant MAX_INSTANT = MAX_DATE_TIME.toInstant(ZoneOffset.UTC);

    public static Instant current() {
        return Instant.now(); //TODO
    }

    public static Instant toInstant(@Nullable LocalDateTime moment) {
        if (isNull(moment)) return null;
        return moment.atZone(ZoneId.systemDefault()).toInstant(); //TODO
    }

    public static Instant toInstant(@Nullable LocalDate date) {
        if (isNull(date)) return null;
        return toInstant(date.atStartOfDay());
    }

    public static OffsetDateTime atOffset(@Nullable Instant instant) {
        if (isNull(instant)) return null;
        return instant.atOffset(ZoneOffset.UTC); //TODO
    }

    public static Instant toInstant(@Nullable OffsetDateTime moment) {
        return isNull(moment) ? MAX_INSTANT : moment.toInstant();
    }
}
