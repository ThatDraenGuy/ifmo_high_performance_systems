package ru.draen.hps.account.app.auth.service;

import ru.draen.hps.account.app.auth.controller.dto.RegisterRequest;
import ru.draen.hps.common.dbms.domain.User;

public interface AuthService {
    User register(RegisterRequest registerRequest);
}
