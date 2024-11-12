package ru.draen.hps.common.core.exception;


public class BadRequestException extends AppException {
    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }
}
