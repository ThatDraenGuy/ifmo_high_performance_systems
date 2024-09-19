package ru.draen.hps.model.user.dao;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.dao.ADeletableRepository;
import ru.draen.hps.domain.User;
import ru.draen.hps.domain.User_;

import java.util.Map;

@Repository
public class UserRepositoryImpl extends ADeletableRepository<User, Long> implements UserRepository {
    @Override
    protected @NonNull Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected void defaultFetchProfile(@NonNull Root<User> root) {
        root.fetch(User_.roles);
    }

    @Override
    protected @NonNull Map<String, Path<?>> getExplicitSortingPaths() {
        return Map.of();
    }
}
