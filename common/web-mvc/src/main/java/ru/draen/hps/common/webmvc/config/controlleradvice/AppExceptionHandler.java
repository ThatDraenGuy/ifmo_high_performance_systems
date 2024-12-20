package ru.draen.hps.common.webmvc.config.controlleradvice;

import jakarta.servlet.http.HttpServletRequest;
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
import ru.draen.hps.common.core.exception.*;
import ru.draen.hps.common.core.model.ErrorResponse;
import ru.draen.hps.common.security.exception.TokenException;

@ControllerAdvice
@Slf4j
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            AppException.class,
            BadRequestException.class,
            NotImplementedException.class,
            NotFoundException.class,
            ProcessingException.class,
            TokenException.class
    })
    private ResponseEntity<Object> handleExceptions(Exception e, HttpServletRequest request) {
        return handleException(e, request, switch (e) {
            case BadRequestException ignored -> HttpStatus.BAD_REQUEST;
            case NotImplementedException ignored -> HttpStatus.NOT_IMPLEMENTED;
            case NotFoundException ignored -> HttpStatus.NOT_FOUND;
            case ProcessingException ignored -> HttpStatus.CONFLICT;
            case TokenException ignored -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        });
    }


    private ResponseEntity<Object> handleException(Exception ex, HttpServletRequest request, HttpStatus status) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(
                new ErrorResponse(status.value(), ex.getMessage(), ex.toString(), request.getServletPath()),
                new HttpHeaders(),
                status
        );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex, Object body,
                                                             @NonNull HttpHeaders headers,
                                                             @NonNull HttpStatusCode statusCode,
                                                             @NonNull WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(
                new ErrorResponse(statusCode.value(), ex.getMessage(), ex.toString(), request.getContextPath()),
                headers, statusCode);
    }
}
