package ru.draen.hps.app.client.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.draen.hps.app.client.controller.dto.ClientCondition;
import ru.draen.hps.app.client.dao.ClientRepository;
import ru.draen.hps.app.client.dao.ClientSpecification;
import ru.draen.hps.common.dao.FetchProfiles;
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.PageResponse;
import ru.draen.hps.common.model.ScrollCondition;
import ru.draen.hps.common.model.ScrollResponse;
import ru.draen.hps.domain.Client;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final TransactionTemplate readOnlyTransactionTemplate;

    @Override
    public PageResponse<Client> findAll(@NonNull ClientCondition condition, @NonNull PageCondition pageCondition) {
        return readOnlyTransactionTemplate.execute(status -> {
            Specification<Client> spec = ClientSpecification.byCondition(condition);
            return new PageResponse<>(
                    clientRepository.findAll(spec, pageCondition),
                    pageCondition,
                    clientRepository.count(spec)
            );
        });
    }

    @Override
    public ScrollResponse<Client> findAll(@NonNull ClientCondition condition, @NonNull ScrollCondition scrollCondition) {
        return readOnlyTransactionTemplate.execute(status -> {
            Specification<Client> spec = ClientSpecification.byCondition(condition);
            return new ScrollResponse<>(
                    clientRepository.findAll(spec, scrollCondition),
                    scrollCondition
            );
        });
    }

    @Override
    public Client findRandom(ClientCondition condition) {
        return readOnlyTransactionTemplate.execute(status -> {
            Specification<Client> spec = ClientSpecification.byCondition(condition);
            Random random = new Random();
            long count = clientRepository.count(spec);
            return clientRepository.findAll(spec,
                            new ScrollCondition(random.nextInt(0, (int)count), 1, Sort.unsorted()))
                    .getFirst();
        });
    }

    @Override
    public Optional<Client> findOneBrief(ClientCondition condition) {
        return readOnlyTransactionTemplate.execute(status -> clientRepository
                .findOne(ClientSpecification.byCondition(condition), FetchProfiles::nothing));
    }
}
