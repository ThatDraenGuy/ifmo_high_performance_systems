package ru.draen.hps.app.cdrfile.service.filename;

import java.time.LocalDate;

public record CdrFileName(
        String fullName,
        String operCode,
        LocalDate periodStart,
        LocalDate periodEnd
) {
}
