package ru.draen.hps.app.cdrfile.service;

import ru.draen.hps.common.exception.AppException;

public class CdrParseException extends AppException {
    public CdrParseException(String message) {
        super(message);
    }

    public CdrParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
