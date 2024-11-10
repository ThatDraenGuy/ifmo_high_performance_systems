package ru.draen.hps.common.webmvc.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.draen.hps.common.security.config.auth.AppUserDetails;

import java.util.Optional;

@FeignClient(value = "user-details-service", url = "account-service:8084", path = "${api.prefix}/users")
public interface UserDetailsClient {
    @GetMapping
    Optional<AppUserDetails> find(@RequestParam("username") String username);
}
