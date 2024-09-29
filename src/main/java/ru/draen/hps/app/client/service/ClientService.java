package ru.draen.hps.app.client.service;

import ru.draen.hps.app.client.controller.dto.ClientCondition;
import ru.draen.hps.common.service.ISearchService;
import ru.draen.hps.domain.Client;

public interface ClientService extends ISearchService<Client, ClientCondition> {
    Client findRandom(ClientCondition condition);
}
