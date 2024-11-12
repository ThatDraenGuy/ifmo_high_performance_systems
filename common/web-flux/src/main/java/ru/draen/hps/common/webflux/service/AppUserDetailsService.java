package ru.draen.hps.common.webflux.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.core.label.ILabelService;
import ru.draen.hps.common.security.I18n;
import ru.draen.hps.common.webflux.client.UserDetailsClient;

@Service
@AllArgsConstructor
public class AppUserDetailsService implements ReactiveUserDetailsService {
    private static final ILabelService lbs = I18n.getLabelService();

    private final UserDetailsClient userDetailsClient;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userDetailsClient.find(username)
                .cast(UserDetails.class)
                .switchIfEmpty(Mono.error(() -> new UsernameNotFoundException(
                        lbs.msg("UsernameNotFoundException.notFound"))));
    }
}
