package ru.draen.hps.common.webmvc.interceptor;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import ru.draen.hps.common.security.config.AppProfile;
import ru.draen.hps.common.security.dto.TokenRequest;
import ru.draen.hps.common.webmvc.client.TokenClient;

@Profile(AppProfile.DEV)
@RequiredArgsConstructor
public class CommonClientConfig {
    private final TokenClient tokenClient;

    @Value("${api.access.username}")
    private String username;

    @Value("${api.access.password}")
    private String password;

    @Bean
    RequestInterceptor interceptor() {
        return new TokenInterceptor(tokenClient, new TokenRequest(username, password));
    }
}
