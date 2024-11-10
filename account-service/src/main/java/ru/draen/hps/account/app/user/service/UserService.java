package ru.draen.hps.account.app.user.service;

import ru.draen.hps.account.app.auth.controller.dto.RegisterRequest;
import ru.draen.hps.common.dbms.domain.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    User register(RegisterRequest registerRequest);
}
