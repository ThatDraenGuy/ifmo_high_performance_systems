package ru.draen.hps.common.webmvc.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.draen.hps.common.core.label.ILabelService;
import ru.draen.hps.common.security.I18n;
import ru.draen.hps.common.webmvc.client.UserDetailsClient;

@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private static final ILabelService lbs = I18n.getLabelService();

    private final UserDetailsClient userDetailsClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDetailsClient.find(username).orElseThrow(() -> new UsernameNotFoundException(
                lbs.msg("UsernameNotFoundException.notFound")));
    }
}
