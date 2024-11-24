package ru.draen.hps.file.config;

import io.rsocket.core.Resume;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.rsocket.server.RSocketServer;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;
import ru.draen.hps.common.core.model.EUserRole;
import ru.draen.hps.common.webflux.config.auth.RequestApplier;

import java.time.Duration;
import java.util.Map;

@Configuration
public class AppConfiguration {
    @Bean
    public RequestApplier requestApplier(@Value("${api.prefix}") String apiPrefix) {
        return http -> http
                .pathMatchers(apiPrefix + "/files/**").hasAuthority(EUserRole.OPERATOR.name())
                .anyExchange().authenticated();
    }

    @Bean
    public RSocketServerCustomizer rSocketResume() {
        Resume resume =
                new Resume()
                        .retry(
                                Retry.fixedDelay(5, Duration.ofSeconds(1)));
        return rSocketServer -> rSocketServer.resume(resume);
    }
}
