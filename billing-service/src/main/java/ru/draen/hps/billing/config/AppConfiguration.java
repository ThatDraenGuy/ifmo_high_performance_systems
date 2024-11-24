package ru.draen.hps.billing.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.draen.hps.common.core.model.EUserRole;
import ru.draen.hps.common.webflux.config.auth.RequestApplier;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Billing Service",
                description = "Billing Service",
                version = "1.0",
                contact = @Contact(
                        name = "ThatDraenGuy"
                )
        ),
        servers = {
                @Server(url = "/billing-service", description = "Gateway server")
        }
)
public class AppConfiguration {
    @Bean
    public RequestApplier requestApplier(@Value("${api.prefix}") String apiPrefix) {
        return http -> http
                .pathMatchers(apiPrefix + "/billing/**").hasAuthority(EUserRole.OPERATOR.name())
                .anyExchange().authenticated();
    }
}
