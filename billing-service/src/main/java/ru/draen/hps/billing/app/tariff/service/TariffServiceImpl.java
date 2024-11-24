package ru.draen.hps.billing.app.tariff.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.publisher.Mono;
import ru.draen.hps.billing.app.tariff.dao.TariffHistRepository;
import ru.draen.hps.billing.app.tariff.dao.TariffRepository;
import ru.draen.hps.billing.app.tariff.dao.TariffToRulesRepository;
import ru.draen.hps.common.dbms.domain.Tariff;
import ru.draen.hps.common.dbms.domain.TariffHist;
import ru.draen.hps.common.jpadao.dao.EntityLoader;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TariffServiceImpl implements TariffService {
    private final TransactionTemplate readOnlyTransactionTemplate;
    private final TariffRepository tariffRepository;
    private final TariffHistRepository tariffHistRepository;

    @Override
    public Optional<Tariff> findById(Long id) {
        return readOnlyTransactionTemplate.execute(status -> tariffRepository.findById(id));
    }

    @Override
    public Mono<TariffHist> findOne(Specification<TariffHist> spec) {
        return Mono.fromSupplier(() ->
                readOnlyTransactionTemplate.execute(status ->
                        tariffHistRepository.findOne(spec).orElse(null)));
    }
}
