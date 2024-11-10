package ru.draen.hps.account.app.user.dao;

import ru.draen.hps.common.dbms.domain.User;
import ru.draen.hps.common.jpadao.dao.ICrudRepository;
import ru.draen.hps.common.jpadao.dao.ISearchRepository;

public interface UserRepository extends ISearchRepository<User, Long>, ICrudRepository<User, Long> {
}
