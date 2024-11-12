package ru.draen.hps.account.app.user.dao;

import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.dbms.domain.User;
import ru.draen.hps.common.dbms.domain.User_;
import ru.draen.hps.common.jpadao.dao.ADeletableRepository;

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
}
