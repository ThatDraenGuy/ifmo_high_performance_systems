package ru.draen.hps.common.exception;

public class NoSuchMessageException extends AppException {
    public NoSuchMessageException(String message) {
        super(message);
    }
    public NoSuchMessageException(Throwable cause) {
        super(cause);
    }
}
