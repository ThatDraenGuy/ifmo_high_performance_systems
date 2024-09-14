package ru.draen.hps.model.user.service;

import ru.draen.hps.common.service.ISearchService;
import ru.draen.hps.domain.User;
import ru.draen.hps.model.user.controller.dto.UserCondition;

public interface UserService extends ISearchService<User, UserCondition> {
}
