package ru.draen.hps.common.webflux.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.core.model.ErrorResponse;
import ru.draen.hps.common.security.config.AppProfile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Profile(AppProfile.DEV)
@Component
public class AuthEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException authException) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                authException.getMessage(),
                authException.toString(),
                exchange.getRequest().getPath().value()
        );

        try {
            final ObjectMapper mapper = new ObjectMapper();
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            mapper.writeValue(byteArray, errorResponse);
            return response.writeWith(Mono.just(new DefaultDataBufferFactory().wrap(byteArray.toByteArray())));
        } catch (IOException e) {
            return Mono.error(e);
        }
    }
}
