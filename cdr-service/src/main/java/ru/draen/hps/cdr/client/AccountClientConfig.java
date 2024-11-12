package ru.draen.hps.cdr.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import reactivefeign.client.ReactiveHttpRequestInterceptor;
import ru.draen.hps.common.security.config.AppProfile;
import ru.draen.hps.common.security.dto.TokenRequest;
import ru.draen.hps.common.webflux.client.TokenClient;
import ru.draen.hps.common.webflux.interceptor.TokenInterceptor;

@Profile(AppProfile.DEV)
@RequiredArgsConstructor
public class AccountClientConfig {
    private final TokenClient tokenClient;

    @Value("${api.access.username}")
    private String username;

    @Value("${api.access.password}")
    private String password;

    @Bean
    ReactiveHttpRequestInterceptor interceptor() {
        return new TokenInterceptor(tokenClient, new TokenRequest(username, password));
    }
}
