package ru.draen.hps.common.webmvc.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.draen.hps.common.security.dto.TokenRequest;
import ru.draen.hps.common.security.dto.TokenResponse;

@FeignClient(value = "account-service", contextId = "accessToken-client", path = "${api.prefix}/auth")
public interface TokenClient {
    @PostMapping("/login")
    TokenResponse getToken(@RequestBody TokenRequest request);
}
