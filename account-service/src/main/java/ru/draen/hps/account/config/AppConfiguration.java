package ru.draen.hps.account.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.draen.hps.common.core.model.EUserRole;
import ru.draen.hps.common.webmvc.config.auth.RequestApplier;

@Configuration
public class AppConfiguration {
    @Bean
    public RequestApplier requestApplier(@Value("${api.prefix}") String apiPrefix) {
        return http -> http
                .requestMatchers(apiPrefix + "/operators/**").hasRole(EUserRole.OPERATOR.name())
                .requestMatchers(apiPrefix + "/clients/**").hasRole(EUserRole.OPERATOR.name())
                .requestMatchers(apiPrefix + "/auth/**").permitAll()
                .anyRequest().permitAll();
    }
}
