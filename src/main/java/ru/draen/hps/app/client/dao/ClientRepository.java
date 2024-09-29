package ru.draen.hps.app.client.dao;

import lombok.NonNull;
import ru.draen.hps.common.dao.ISearchRepository;
import ru.draen.hps.domain.Client;

public interface ClientRepository extends ISearchRepository<Client, Long> {
    Client save(@NonNull Client client);
}
