package ru.draen.hps.app.tariffrule.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.draen.hps.app.tariffrule.controller.dto.TariffRuleCondition;
import ru.draen.hps.app.tariffrule.dao.TariffRuleRepository;
import ru.draen.hps.app.tariffrule.dao.TariffRuleSpecification;
import ru.draen.hps.common.dao.EntityLoader;
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.PageResponse;
import ru.draen.hps.common.model.ScrollCondition;
import ru.draen.hps.common.model.ScrollResponse;
import ru.draen.hps.domain.TariffRule;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TariffRuleServiceImpl implements TariffRuleService {
    private final TransactionTemplate transactionTemplate;
    private final TransactionTemplate readonlyTransactionTemplate;
    private final TariffRuleRepository tariffRuleRepository;
    private final EntityLoader entityLoader;

    @Override
    public Optional<TariffRule> getById(@NonNull Long id) {
        return readonlyTransactionTemplate.execute(status -> tariffRuleRepository.findById(id));
    }

    @Override
    public TariffRule create(@NonNull TariffRule entity) {
        return transactionTemplate.execute(status -> {
            prepareToCreate(entity);
            return tariffRuleRepository.save(entity);
        });
    }

    @Override
    public boolean delete(Long id) {
        return Boolean.TRUE.equals(transactionTemplate.execute(status -> tariffRuleRepository.findById(id)
                .map(entity -> {
                    checkDependencies(entity);
                    tariffRuleRepository.delete(entity);
                    return true;
                })
                .isPresent()));
    }

    @Override
    public PageResponse<TariffRule> findAll(@NonNull TariffRuleCondition condition, @NonNull PageCondition pageCondition) {
        return readonlyTransactionTemplate.execute(status -> {
            Specification<TariffRule> spec = TariffRuleSpecification.byCondition(condition);
            return new PageResponse<>(
                    tariffRuleRepository.findAll(spec, pageCondition),
                    pageCondition,
                    tariffRuleRepository.count(spec)
            );
        });
    }

    @Override
    public ScrollResponse<TariffRule> findAll(@NonNull TariffRuleCondition condition, @NonNull ScrollCondition scrollCondition) {
        return readonlyTransactionTemplate.execute(status -> {
            Specification<TariffRule> spec = TariffRuleSpecification.byCondition(condition);
            return new ScrollResponse<>(
                    tariffRuleRepository.findAll(spec, scrollCondition),
                    scrollCondition
            );
        });
    }

    private void prepareToCreate(TariffRule entity) {
        entity.setOperator(entityLoader.load(entity.getOperator()));
    }

    private void checkDependencies(TariffRule entity) {

    }
}
