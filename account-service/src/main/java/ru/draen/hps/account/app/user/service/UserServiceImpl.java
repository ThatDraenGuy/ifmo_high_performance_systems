package ru.draen.hps.account.app.user.service;

import com.hazelcast.map.IMap;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.draen.hps.account.I18n;
import ru.draen.hps.account.app.auth.controller.dto.RegisterRequest;
import ru.draen.hps.account.app.user.dao.UserRepository;
import ru.draen.hps.account.app.user.dao.UserSpecification;
import ru.draen.hps.common.core.exception.AppException;
import ru.draen.hps.common.core.label.ILabelService;
import ru.draen.hps.common.core.model.EUserRole;
import ru.draen.hps.common.dbms.domain.User;
import ru.draen.hps.common.security.config.auth.AppUserDetails;
import ru.draen.hps.common.webmvc.utils.CacheUtils;

import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final TransactionTemplate readOnlyTransactionTemplate;
    private final UserRepository userRepository;
    private final IMap<String, User> userCache;

    @Override
    public Optional<User> findByUsername(String username) {
        return CacheUtils.cacheGet(userCache, username, () ->
                readOnlyTransactionTemplate.execute(status -> {
                    Specification<User> spec = UserSpecification.byUsername(username);
                    return userRepository.findOne(spec);
                }));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username)
                .map(user -> new AppUserDetails(user.getUsername(), user.getPassword(), user.getRoles()))
                .orElseThrow(() -> new UsernameNotFoundException(ru.draen.hps.common.security.I18n.getLabelService()
                        .msg("UsernameNotFoundException.notFound")));
    }
}
