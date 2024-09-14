package ru.draen.hps.model.user.dao;

import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.dao.AGenericRepository;
import ru.draen.hps.domain.User;
import ru.draen.hps.domain.User_;

@Repository
public class UserRepositoryImpl extends AGenericRepository<User, Long> implements UserRepository {
    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected void defaultFetchProfile(Root<User> root) {
        root.fetch(User_.roles);
    }
}
