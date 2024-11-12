package ru.draen.hps.common.webflux.interceptor;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import reactivefeign.client.ReactiveHttpRequest;
import reactivefeign.client.ReactiveHttpRequestInterceptor;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.webflux.client.TokenClient;
import ru.draen.hps.common.security.dto.TokenRequest;
import ru.draen.hps.common.security.dto.TokenResponse;

import java.util.List;

@AllArgsConstructor
public class TokenInterceptor implements ReactiveHttpRequestInterceptor {
    private final TokenClient tokenClient;
    private final TokenRequest tokenRequest;

    @Override
    public Mono<ReactiveHttpRequest> apply(ReactiveHttpRequest reactiveHttpRequest) {
        return tokenClient.getToken(tokenRequest)
                .map(TokenResponse::accessToken)
                .map(token -> {
                    reactiveHttpRequest.headers().put(HttpHeaders.AUTHORIZATION, List.of("Bearer " + token));
                    return reactiveHttpRequest;
                });
    }
}
