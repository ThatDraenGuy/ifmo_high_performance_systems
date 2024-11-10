package ru.draen.hps.common.webflux.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.security.config.auth.AppUserDetails;

@ReactiveFeignClient(value = "user-details-service", url = "account-service:8084", path = "${api.prefix}/users")
public interface UserDetailsClient {
    @GetMapping
    Mono<AppUserDetails> find(@RequestParam("username") String username);
}
