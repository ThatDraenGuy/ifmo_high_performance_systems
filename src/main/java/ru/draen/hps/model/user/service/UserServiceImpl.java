package ru.draen.hps.model.user.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.PageResponse;
import ru.draen.hps.common.model.ScrollCondition;
import ru.draen.hps.common.model.ScrollResponse;
import ru.draen.hps.domain.User;
import ru.draen.hps.model.user.controller.dto.UserCondition;
import ru.draen.hps.model.user.dao.UserRepository;
import ru.draen.hps.model.user.dao.UserSpecification;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final TransactionTemplate readOnlyTransactionTemplate;
    private final UserRepository userRepository;

    @Override
    public PageResponse<User> findAll(@NonNull UserCondition condition, @NonNull PageCondition pageCondition) {
        return readOnlyTransactionTemplate.execute(status -> {
            final Specification<User> spec = UserSpecification.getByCondition(condition);
            return new PageResponse<>(
                    userRepository.findAll(spec, pageCondition),
                    pageCondition,
                    userRepository.count(spec)
            );
        });
    }

    @Override
    public ScrollResponse<User> findAll(@NonNull UserCondition condition, @NonNull ScrollCondition scrollCondition) {
        return readOnlyTransactionTemplate.execute(status -> {
            final Specification<User> spec = UserSpecification.getByCondition(condition);
            return new ScrollResponse<>(
                    userRepository.findAll(spec, scrollCondition),
                    scrollCondition
            );
        });
    }
}
