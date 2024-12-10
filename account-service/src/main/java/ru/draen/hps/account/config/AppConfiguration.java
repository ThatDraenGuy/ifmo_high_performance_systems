package ru.draen.hps.account.config;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.draen.hps.common.dbms.domain.Client;
import ru.draen.hps.common.dbms.domain.Operator;
import ru.draen.hps.common.dbms.domain.User;
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
                @Server(url = "${app.openapi.prefix}/account-service", description = "Gateway server")
        }
)
public class AppConfiguration {
    @Bean
    public RequestApplier requestApplier(@Value("${api.prefix}") String apiPrefix,
                                         @Value("${springdoc.api-docs.path}") String docsPath) {
        return http -> http
                .requestMatchers(apiPrefix + "/operators/**").permitAll()
                .requestMatchers(apiPrefix + "/clients/**").permitAll()
                .requestMatchers(apiPrefix + "/auth/**").permitAll()
                .requestMatchers(docsPath).permitAll()
                .anyRequest().permitAll();
    }

    @Bean
    public IMap<String, User> userCache(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getMap("users");
    }

    @Bean
    public IMap<Long, Operator> operatorCache(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getMap("operators");
    }

    @Bean
    public IMap<Long, Client> clientCache(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getMap("clients");
    }

    @Bean
    public IMap<String, Client> clientPhoneCache(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getMap("clients-phones");
    }
}
