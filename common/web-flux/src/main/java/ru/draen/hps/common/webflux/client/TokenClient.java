package ru.draen.hps.common.webflux.client;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.security.dto.TokenRequest;
import ru.draen.hps.common.security.dto.TokenResponse;

@ReactiveFeignClient(value = "account-service", qualifier = "accessToken-client", path = "${api.prefix}/auth")
public interface TokenClient {
    @PostMapping("/login")
    Mono<TokenResponse> getToken(@RequestBody TokenRequest request);
}
