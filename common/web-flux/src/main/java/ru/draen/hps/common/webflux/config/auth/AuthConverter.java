package ru.draen.hps.common.webflux.config.auth;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.security.config.AppProfile;
import ru.draen.hps.common.security.config.auth.JwtUtils;

@Profile(AppProfile.DEV)
@Component
@AllArgsConstructor
public class AuthConverter implements ServerAuthenticationConverter {
    private final JwtUtils jwtUtils;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .map(jwtUtils::extractAccessToken)
                .map(JwtTokenAuthentication::new);
    }
}
