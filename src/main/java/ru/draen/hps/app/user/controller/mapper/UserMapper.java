package ru.draen.hps.app.user.controller.mapper;

import ru.draen.hps.common.annotation.Mapper;
import ru.draen.hps.common.mapper.IMapper;
import ru.draen.hps.domain.User;
import ru.draen.hps.app.user.controller.dto.UserDto;

@Mapper
public class UserMapper implements IMapper<User, UserDto> {
    @Override
    public User toEntity(UserDto dto) {
        return null;
    }

    @Override
    public UserDto toDto(User entity) {
        return null;
    }

    @Override
    public UserDto toId(User entity) {
        return null;
    }
}
