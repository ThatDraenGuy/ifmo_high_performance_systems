package ru.draen.hps.account.app.operator.service;

import lombok.NonNull;
import ru.draen.hps.common.dbms.domain.Operator;

import java.util.Optional;

public interface OperatorService {
    Optional<Operator> getById(@NonNull Long id);
}
