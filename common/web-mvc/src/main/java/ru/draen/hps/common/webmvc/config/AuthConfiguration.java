package ru.draen.hps.common.webmvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.draen.hps.common.security.config.AppProfile;
import ru.draen.hps.common.webmvc.config.auth.RequestApplier;
import ru.draen.hps.common.webmvc.config.auth.AuthEntryPoint;
import ru.draen.hps.common.webmvc.config.auth.AuthFilter;

@Profile(AppProfile.DEV)
@Configuration
@EnableWebSecurity
public class AuthConfiguration {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthFilter authFilter, AuthEntryPoint authEntryPoint,
                                                   RequestApplier requestApplier) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requestApplier::apply)
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(authEntryPoint));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
