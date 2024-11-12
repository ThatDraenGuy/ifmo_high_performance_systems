package ru.draen.hps.account.app.auth.service;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
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
import ru.draen.hps.common.security.config.AppProfile;

import java.util.Set;

@Profile(AppProfile.DEV)
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ILabelService lbs = I18n.getLabelService();
    private final TransactionTemplate transactionTemplate;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


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
