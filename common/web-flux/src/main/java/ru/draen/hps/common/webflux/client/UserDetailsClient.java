package ru.draen.hps.common.webflux.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.security.config.auth.AppUserDetails;
import ru.draen.hps.common.webflux.interceptor.CommonClientConfig;

@ReactiveFeignClient(value = "account-service", qualifier = "user-details-client", path = "${api.prefix}/users",
        configuration = CommonClientConfig.class)
public interface UserDetailsClient {
    @GetMapping
    Mono<AppUserDetails> find(@RequestParam("username") String username);
}
