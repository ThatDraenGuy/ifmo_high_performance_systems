package ru.draen.hps.common.webflux.config.controlleradvice;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
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
            TokenException.class,
            AccessDeniedException.class,
            Exception.class
    })
    private ResponseEntity<Object> handleExceptions(Exception e, ServerWebExchange exchange) {
        return handleException(e, exchange, switch (e) {
            case BadRequestException ex -> HttpStatus.BAD_REQUEST;
            case NotImplementedException ex -> HttpStatus.NOT_IMPLEMENTED;
            case NotFoundException ex -> HttpStatus.NOT_FOUND;
            case ProcessingException ex -> HttpStatus.CONFLICT;
            case TokenException ex -> HttpStatus.FORBIDDEN;
            case AccessDeniedException ex -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        });
    }


    private ResponseEntity<Object> handleException(Exception ex, ServerWebExchange exchange, HttpStatus status) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(
                new ErrorResponse(status.value(), ex.getMessage(), ex.toString(), exchange.getRequest().getPath().value()),
                new HttpHeaders(),
                status
        );
    }

    @Override
    protected Mono<ResponseEntity<Object>> handleExceptionInternal(@NonNull Exception ex, @Nullable Object body,
                                                                   @Nullable HttpHeaders headers,
                                                                   @NonNull HttpStatusCode statusCode,
                                                                   ServerWebExchange exchange) {
        log.error(ex.getMessage(), ex);
        return Mono.just(new ResponseEntity<>(
                new ErrorResponse(statusCode.value(), ex.getMessage(), ex.toString(), exchange.getRequest().getPath().value()),
                headers, statusCode));
    }
}
