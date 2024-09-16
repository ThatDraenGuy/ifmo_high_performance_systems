package ru.draen.hps.common.exception;

public class NotFoundException extends AppException {
    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }
}
