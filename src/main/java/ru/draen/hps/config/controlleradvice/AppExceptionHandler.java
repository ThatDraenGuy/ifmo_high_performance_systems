package ru.draen.hps.config.controlleradvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.draen.hps.I18n;
import ru.draen.hps.common.exception.*;
import ru.draen.hps.common.label.ILabelService;
import ru.draen.hps.common.model.ErrorResponse;

@ControllerAdvice
@Slf4j
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    private static final ILabelService lbs = I18n.getLabelService();

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
            case BadRequestException ex -> HttpStatus.BAD_REQUEST;
            case NotImplementedException ex -> HttpStatus.NOT_IMPLEMENTED;
            case NotFoundException ex -> HttpStatus.NOT_FOUND;
            case ProcessingException ex -> HttpStatus.CONFLICT;
            case TokenException ex -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        });
    }

    @ExceptionHandler(BadCredentialsException.class)
    private ResponseEntity<Object> handleBadCredentials(BadCredentialsException e, HttpServletRequest request) {
        return handleException(new AuthException(lbs.msg("AuthException.badCredentials"), e),
                request, HttpStatus.UNAUTHORIZED);
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
