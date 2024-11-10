package ru.draen.hps.common.webflux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import ru.draen.hps.common.security.config.AppProfile;
import ru.draen.hps.common.webflux.config.auth.*;

@Profile(AppProfile.DEV)
@Configuration
@EnableWebFluxSecurity
public class AuthConfiguration {
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http, AuthEntryPoint authEntryPoint,
                                                      AuthManager authManager, AuthConverter authConverter,
                                                      RequestApplier requestApplier) throws Exception {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authManager);
        authenticationWebFilter.setServerAuthenticationConverter(authConverter);
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeExchange(requestApplier::apply)
                .authenticationManager(authManager)
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(authEntryPoint));
        return http.build();
    }
}
