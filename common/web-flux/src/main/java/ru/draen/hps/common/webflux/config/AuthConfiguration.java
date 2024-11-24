package ru.draen.hps.common.webflux.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import ru.draen.hps.common.security.config.AppProfile;
import ru.draen.hps.common.webflux.config.auth.*;

@Profile(AppProfile.DEV)
@Configuration
@EnableWebFluxSecurity
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class AuthConfiguration {
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http, AuthEntryPoint authEntryPoint,
                                                      AccessDeniedHandler accessDeniedHandler,
                                                      AuthManager authManager, AuthConverter authConverter,
                                                      RequestApplier requestApplier) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authManager);
        authenticationWebFilter.setServerAuthenticationConverter(authConverter);
        authenticationWebFilter.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(authEntryPoint));
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeExchange(requestApplier::apply)
                .authenticationManager(authManager)
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authEntryPoint));
        return http.build();
    }
}
