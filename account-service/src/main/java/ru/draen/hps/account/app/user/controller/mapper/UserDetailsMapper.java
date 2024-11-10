package ru.draen.hps.account.app.user.controller.mapper;

import ru.draen.hps.common.core.annotation.Mapper;
import ru.draen.hps.common.core.exception.NotImplementedException;
import ru.draen.hps.common.core.mapper.IMapper;
import ru.draen.hps.common.dbms.domain.User;
import ru.draen.hps.common.security.config.auth.AppUserDetails;

@Mapper
public class UserDetailsMapper implements IMapper<User, AppUserDetails> {
    @Override
    public User toEntity(AppUserDetails dto) {
        throw new NotImplementedException();
    }

    @Override
    public AppUserDetails toDto(User entity) {
        return new AppUserDetails(entity.getUsername(), entity.getPassword(), entity.getRoles());
    }
}
