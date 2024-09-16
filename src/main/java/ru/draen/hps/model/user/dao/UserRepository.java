package ru.draen.hps.model.user.dao;

import ru.draen.hps.common.dao.ICrudRepository;
import ru.draen.hps.common.dao.IDeletableRepository;
import ru.draen.hps.common.dao.ISearchRepository;
import ru.draen.hps.domain.User;

public interface UserRepository extends ISearchRepository<User, Long>,
        ICrudRepository<User, Long>, IDeletableRepository<User, Long> {
}
