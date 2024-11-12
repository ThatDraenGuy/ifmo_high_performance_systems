package ru.draen.hps.common.webmvc.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.draen.hps.common.security.config.auth.AppUserDetails;
import ru.draen.hps.common.webmvc.interceptor.CommonClientConfig;

import java.util.Optional;

@FeignClient(value = "account-service", contextId = "user-details-client", path = "${api.prefix}/users",
        configuration = CommonClientConfig.class)
public interface UserDetailsClient {
    @GetMapping
    Optional<AppUserDetails> find(@RequestParam("username") String username);
}
