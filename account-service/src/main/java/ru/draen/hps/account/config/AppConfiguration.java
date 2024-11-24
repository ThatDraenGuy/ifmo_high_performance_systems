package ru.draen.hps.account.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.draen.hps.common.webmvc.config.auth.RequestApplier;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Account Service",
                description = "Account Service",
                version = "1.0",
                contact = @Contact(
                        name = "ThatDraenGuy"
                )
        ),
        servers = {
                @Server(url = "/account-service", description = "Gateway server")
        }
)
public class AppConfiguration {
    @Bean
    public RequestApplier requestApplier(@Value("${api.prefix}") String apiPrefix) {
        return http -> http
                .requestMatchers(apiPrefix + "/operators/**").permitAll()
                .requestMatchers(apiPrefix + "/clients/**").permitAll()
                .requestMatchers(apiPrefix + "/auth/**").permitAll()
                .anyRequest().permitAll();
    }
}
