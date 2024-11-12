package ru.draen.hps.cdr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import ru.draen.hps.common.core.model.EUserRole;
import ru.draen.hps.common.webflux.config.auth.RequestApplier;

@Configuration
public class AppConfiguration {
    @Bean
    public RequestApplier requestApplier(@Value("${api.prefix}") String apiPrefix) {
        return http -> http
                .pathMatchers(HttpMethod.GET, apiPrefix + "/cdr-files/**").hasAnyAuthority(
                        EUserRole.OPERATOR.name(), EUserRole.CLIENT.name())
                .pathMatchers(apiPrefix + "/cdr-files/**").hasAuthority(EUserRole.OPERATOR.name())
                .anyExchange().authenticated();
    }
}
