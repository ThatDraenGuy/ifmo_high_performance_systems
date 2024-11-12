package ru.draen.hps.common.csv;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opencsv.exceptions.CsvException;

public record CsvError(
        String message,
        @JsonIgnore Throwable cause,
        Long lineNumber,
        String row
) {
    public CsvError(CsvException exception) {
        this(
                exception.getMessage(),
                exception.getCause(),
                exception.getLineNumber(),
                String.join(",", exception.getLine())
        );
    }
}
