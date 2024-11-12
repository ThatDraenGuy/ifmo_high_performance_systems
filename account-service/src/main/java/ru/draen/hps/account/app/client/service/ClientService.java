package ru.draen.hps.account.app.client.service;

import lombok.NonNull;
import ru.draen.hps.account.app.client.controller.dto.ClientCondition;
import ru.draen.hps.common.dbms.domain.Client;

import java.util.Optional;

public interface ClientService {
    Optional<Client> findById(@NonNull Long clientId);
    Optional<Client> findOne(@NonNull ClientCondition condition);
}
