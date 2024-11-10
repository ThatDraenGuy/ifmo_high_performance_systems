package ru.draen.hps.common.webflux.config.auth;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthManager implements ReactiveAuthenticationManager {
    private final ReactiveUserDetailsService userDetailsService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        if (authentication instanceof JwtTokenAuthentication jwtToken) {
            Mono<UserDetails> userDetails = userDetailsService.findByUsername(jwtToken.getAccessToken().username());
            return userDetails.map(details -> new UsernamePasswordAuthenticationToken(
                    details, null, details.getAuthorities()));
        }
        return Mono.just(authentication);
    }
}