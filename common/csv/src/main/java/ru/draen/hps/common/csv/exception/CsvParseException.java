package ru.draen.hps.common.csv.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.draen.hps.common.core.exception.ProcessingException;
import ru.draen.hps.common.csv.CsvError;

import java.util.List;

@Slf4j
public class CsvParseException extends ProcessingException {

    @SneakyThrows
    public CsvParseException(List<CsvError> errors) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.error(objectMapper.writeValueAsString(errors));
    }
}
