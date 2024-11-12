package ru.draen.hps.common.core.exception;

public class NotFoundException extends AppException {
    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }
}
