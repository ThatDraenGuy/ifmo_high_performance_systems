package ru.draen.hps.cdr.service;


import ru.draen.hps.common.core.exception.AppException;

public class CdrParseException extends AppException {
    public CdrParseException(String message) {
        super(message);
    }

    public CdrParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
