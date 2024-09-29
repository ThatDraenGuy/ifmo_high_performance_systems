package ru.draen.hps.app.client.dao;

import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.dao.ADeletableRepository;
import ru.draen.hps.domain.Client;
import ru.draen.hps.domain.Client_;

@Repository
public class ClientRepositoryImpl extends ADeletableRepository<Client, Long> implements ClientRepository {
    @Override
    protected @NonNull Class<Client> getEntityClass() {
        return Client.class;
    }

    @Override
    protected void defaultFetchProfile(@NonNull Root<Client> root) {
        root.fetch(Client_.operator);
    }
}
