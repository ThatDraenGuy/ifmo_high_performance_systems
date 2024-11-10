package ru.draen.hps.account.app.user.service;

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

import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final ILabelService lbs = I18n.getLabelService();

    private final TransactionTemplate readOnlyTransactionTemplate;
    private final TransactionTemplate transactionTemplate;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByUsername(String username) {
        return readOnlyTransactionTemplate.execute(status -> {
            Specification<User> spec = UserSpecification.byUsername(username);
            return userRepository.findOne(spec);
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByUsername(username)
                .map(user -> new AppUserDetails(user.getUsername(), user.getPassword(), user.getRoles()))
                .orElseThrow(() -> new UsernameNotFoundException(ru.draen.hps.common.security.I18n.getLabelService()
                        .msg("UsernameNotFoundException.notFound")));
    }

    @Override
    public User register(RegisterRequest registerRequest) {
        return transactionTemplate.execute(status -> {
            if (userRepository.exists(UserSpecification.byUsername(registerRequest.username()))) {
                throw new AppException(lbs.msg("AppException.registration.usernameExists"));
            }
            String encodedPassword = passwordEncoder.encode(registerRequest.password());
            User entity = new User();
            entity.setUsername(registerRequest.username());
            entity.setPassword(encodedPassword);
            entity.setRoles(Set.of(EUserRole.CLIENT));
            return userRepository.save(entity);
        });
    }


}
