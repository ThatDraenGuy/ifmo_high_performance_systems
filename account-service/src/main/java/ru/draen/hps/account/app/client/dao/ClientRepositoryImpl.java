package ru.draen.hps.account.app.client.dao;

import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.dbms.domain.Client;
import ru.draen.hps.common.dbms.domain.Client_;
import ru.draen.hps.common.jpadao.dao.ADeletableRepository;

@Repository
public class ClientRepositoryImpl extends ADeletableRepository<Client, Long> implements ClientRepository {
    @Override
    protected @NonNull Class<Client> getEntityClass() {
        return Client.class;
    }

    @Override
    protected void defaultFetchProfile(@NonNull Root<Client> root) {
        root.fetch(Client_.operator);
        root.fetch(Client_.tariff);
    }
}
