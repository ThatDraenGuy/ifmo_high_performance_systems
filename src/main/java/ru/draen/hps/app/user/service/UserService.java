package ru.draen.hps.app.user.service;

import ru.draen.hps.common.service.ISearchService;
import ru.draen.hps.domain.User;
import ru.draen.hps.app.user.controller.dto.UserCondition;

public interface UserService extends ISearchService<User, UserCondition> {
}
