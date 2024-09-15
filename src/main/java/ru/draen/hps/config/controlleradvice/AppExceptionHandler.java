package ru.draen.hps.config.controlleradvice;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.draen.hps.common.exception.AppException;
import ru.draen.hps.common.exception.BadRequestException;
import ru.draen.hps.common.exception.NotFoundException;
import ru.draen.hps.common.exception.NotImplementedException;
import ru.draen.hps.common.model.ErrorResponse;

@ControllerAdvice
@Slf4j
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            AppException.class,
            BadRequestException.class,
            NotImplementedException.class,
            NotFoundException.class
    })
    private ResponseEntity<Object> handleExceptions(Exception e, WebRequest request) {
        return handleException(e, request, switch (e) {
            case BadRequestException ex -> HttpStatus.BAD_REQUEST;
            case NotImplementedException ex -> HttpStatus.NOT_IMPLEMENTED;
            case NotFoundException ex -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        });
    }

    private ResponseEntity<Object> handleException(Exception ex, WebRequest request, HttpStatus status) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex, Object body,
                                                             @NonNull HttpHeaders headers,
                                                             @NonNull HttpStatusCode statusCode,
                                                             @NonNull WebRequest request) {
        log.error(ex.getMessage() ,ex);
        return new ResponseEntity<>(new ErrorResponse(statusCode.value(), ex.getMessage(), ex.toString()), headers, statusCode);
    }
}
