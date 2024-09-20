package ru.draen.hps.app.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.draen.hps.common.mapper.IMapper;
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.PageResponse;
import ru.draen.hps.common.model.ScrollCondition;
import ru.draen.hps.common.model.ScrollResponse;
import ru.draen.hps.domain.User;
import ru.draen.hps.app.user.controller.dto.UserCondition;
import ru.draen.hps.app.user.controller.dto.UserDto;
import ru.draen.hps.app.user.service.UserService;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final IMapper<User, UserDto> userMapper;

    @GetMapping("/paged")
    public PageResponse<UserDto> findPage(@Validated UserCondition userCondition, PageCondition pageCondition) {
        return userService.findAll(userCondition, pageCondition).map(userMapper::toDto);
    }

    @GetMapping
    public ScrollResponse<UserDto> find(@Validated UserCondition userCondition, ScrollCondition scrollCondition) {
        return userService.findAll(userCondition, scrollCondition).map(userMapper::toDto);
    }
}
