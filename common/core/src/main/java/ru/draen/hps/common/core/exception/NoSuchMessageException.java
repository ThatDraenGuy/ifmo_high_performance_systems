package ru.draen.hps.common.core.exception;

public class NoSuchMessageException extends AppException {
    public NoSuchMessageException(String message) {
        super(message);
    }
    public NoSuchMessageException(Throwable cause) {
        super(cause);
    }
}
