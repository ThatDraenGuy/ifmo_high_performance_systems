package ru.draen.hps.app.auth.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.draen.hps.I18n;
import ru.draen.hps.app.user.dao.UserRepository;
import ru.draen.hps.app.user.dao.UserSpecification;
import ru.draen.hps.common.label.ILabelService;
import ru.draen.hps.config.auth.AppUserDetails;

@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private static final ILabelService lbs = I18n.getLabelService();

    private final TransactionTemplate readOnlyTransactionTemplate;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return readOnlyTransactionTemplate.execute(status ->
                userRepository.findOne(UserSpecification.getByUsername(username))
                        .map(AppUserDetails::new)
                        .orElseThrow(() -> new UsernameNotFoundException(
                                lbs.msg("UsernameNotFoundException.notFound"))));
    }
}
