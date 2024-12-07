package ru.draen.hps.account.app.client.service;

import com.hazelcast.map.IMap;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.draen.hps.account.app.client.controller.dto.ClientCondition;
import ru.draen.hps.account.app.client.dao.ClientRepository;
import ru.draen.hps.account.app.client.dao.ClientSpecification;
import ru.draen.hps.common.dbms.domain.Client;
import ru.draen.hps.common.webmvc.utils.CacheUtils;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final TransactionTemplate readOnlyTransactionTemplate;
    private final ClientRepository clientRepository;
    private final IMap<Long, Client> clientCache;
    private final IMap<String, Client> clientPhoneCache;

    @Override
    public Optional<Client> findById(@NonNull Long clientId) {
        return CacheUtils.cacheGet(clientCache, clientId, () ->
                readOnlyTransactionTemplate.execute(status ->
                        clientRepository.findById(clientId)));
    }

    @Override
    public Optional<Client> findOne(@NonNull ClientCondition condition) {
        return CacheUtils.cacheGet(clientPhoneCache, condition.phoneNumber(), () ->
                readOnlyTransactionTemplate.execute(status -> {
                    Specification<Client> spec = ClientSpecification.byCondition(condition);
                    return clientRepository.findOne(spec);
                }));
    }
}
