package ru.draen.hps.common.webmvc.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import ru.draen.hps.common.security.dto.TokenRequest;
import ru.draen.hps.common.webmvc.client.TokenClient;

@AllArgsConstructor
public class TokenInterceptor implements RequestInterceptor {
    private final TokenClient tokenClient;
    private final TokenRequest tokenRequest;
    @Override
    public void apply(RequestTemplate template) {
        String token = tokenClient.getToken(tokenRequest).accessToken();
        template.header(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
