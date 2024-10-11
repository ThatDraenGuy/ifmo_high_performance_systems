package ru.draen.hps.app.tariff.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.draen.hps.app.tariff.controller.dto.TariffCondition;
import ru.draen.hps.app.tariff.dao.TariffHistRepository;
import ru.draen.hps.app.tariff.dao.TariffHistSpecification;
import ru.draen.hps.app.tariff.dao.TariffRepository;
import ru.draen.hps.app.tariff.dao.TariffToRulesRepository;
import ru.draen.hps.common.dao.EntityLoader;
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.PageResponse;
import ru.draen.hps.common.model.ScrollCondition;
import ru.draen.hps.common.model.ScrollResponse;
import ru.draen.hps.domain.Operator_;
import ru.draen.hps.domain.Tariff;
import ru.draen.hps.domain.TariffHist;
import ru.draen.hps.domain.Tariff_;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class TariffServiceImpl implements TariffService {
    private final TransactionTemplate transactionTemplate;
    private final TransactionTemplate readOnlyTransactionTemplate;
    private final TariffRepository tariffRepository;
    private final TariffHistRepository tariffHistRepository;
    private final TariffToRulesRepository tariffToRulesRepository;
    private final EntityLoader entityLoader;

    @Override
    public Optional<TariffHist> getById(@NonNull Long id) {
        return readOnlyTransactionTemplate.execute(status -> tariffHistRepository.findById(id));
    }

    @Override
    public TariffHist create(@NonNull TariffHist entity) {
        return transactionTemplate.execute(status -> {
            validate(entity);
            prepareToSave(entity);
            tariffRepository.save(entity.getTariff());
            tariffHistRepository.saveOrUpdate(entity);
            tariffToRulesRepository.save(entity.getRules());
            return entity;
        });
    }

    @Override
    public Optional<TariffHist> update(@NonNull TariffHist entity) {
        return transactionTemplate.execute(status -> {
            validate(entity);
            if (tariffHistRepository.exists(entity)) {
                prepareToSave(entity);
                tariffHistRepository.saveOrUpdate(entity);
                tariffToRulesRepository.save(entity.getRules());
                return Optional.of(entity);
            }
            return Optional.empty();
        });
    }

    @Override
    public boolean delete(Long id) {
        return Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            Specification<TariffHist> spec = TariffHistSpecification.logicalKey(id);
            return tariffHistRepository.delete(spec);
        }));
    }

    private void validate(TariffHist entity) {
        //TODO
    }

    private void prepareToSave(TariffHist entity) {
        entity.getTariff().setOperator(entityLoader.load(entity.getTariff().getOperator()));
        entity.getRules().forEach(rule -> rule.setTariffRule(entityLoader.load(rule.getTariffRule())));
    }

    @Override
    public PageResponse<TariffHist> findAll(@NonNull TariffCondition condition, @NonNull PageCondition pageCondition) {
        return readOnlyTransactionTemplate.execute(status -> {
            Specification<TariffHist> spec = TariffHistSpecification.byCondition(condition);
            return new PageResponse<>(
                    tariffHistRepository.findAll(spec, pageCondition),
                    pageCondition,
                    tariffHistRepository.count(spec)
            );
        });
    }

    @Override
    public ScrollResponse<TariffHist> findAll(@NonNull TariffCondition condition, @NonNull ScrollCondition scrollCondition) {
        return readOnlyTransactionTemplate.execute(status -> {
            Specification<TariffHist> spec = TariffHistSpecification.byCondition(condition);
            return new ScrollResponse<>(
                    tariffHistRepository.findAll(spec, scrollCondition),
                    scrollCondition
            );
        });
    }

    @Override
    public Tariff findRandom(Long operatorId) {
        return readOnlyTransactionTemplate.execute(status -> {
            Specification<Tariff> spec = (root, cq, cb) -> cb.equal(root.get(Tariff_.operator).get(Operator_.id), operatorId);
            Random random = new Random();
            long count = tariffRepository.count(spec);
            return tariffRepository.findAll(spec, new ScrollCondition(random.nextInt(0, (int)count), 1, Sort.unsorted()))
                    .getFirst();
        });
    }

    @Override
    public Optional<Tariff> findById(Long id) {
        return readOnlyTransactionTemplate.execute(status -> tariffRepository.findById(id));
    }
}
