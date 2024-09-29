package ru.draen.hps.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.draen.hps.common.csv.CsvError;

import java.util.List;

@Slf4j
public class CsvParseException extends ProcessingException {
    private final List<CsvError> errors;

    @SneakyThrows
    public CsvParseException(List<CsvError> errors) {
        this.errors = errors;
        ObjectMapper objectMapper = new ObjectMapper();
        log.error(objectMapper.writeValueAsString(errors));
    }
}
