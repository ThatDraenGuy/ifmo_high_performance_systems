package ru.draen.hps.app.operator.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.draen.hps.app.operator.controller.dto.OperatorCondition;
import ru.draen.hps.app.operator.dao.OperatorFetchProfile;
import ru.draen.hps.app.operator.dao.OperatorRepository;
import ru.draen.hps.app.operator.dao.OperatorSpecification;
import ru.draen.hps.common.dao.EntityLoader;
import ru.draen.hps.common.exception.NotImplementedException;
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.PageResponse;
import ru.draen.hps.common.model.ScrollCondition;
import ru.draen.hps.common.model.ScrollResponse;
import ru.draen.hps.domain.Operator;

import java.util.Optional;

@Service
@AllArgsConstructor
public class OperatorServiceImpl implements OperatorService {
    private final TransactionTemplate transactionTemplate;
    private final TransactionTemplate readOnlyTransactionTemplate;
    private final EntityLoader entityLoader;
    private final OperatorRepository operatorRepository;

    @Override
    public PageResponse<Operator> findAll(@NonNull OperatorCondition condition, @NonNull PageCondition pageCondition) {
        return readOnlyTransactionTemplate.execute(status -> {
            Specification<Operator> spec = OperatorSpecification.byCondition(condition);
            return new PageResponse<>(
                    operatorRepository.findAll(spec, pageCondition, OperatorFetchProfile::all),
                    pageCondition,
                    operatorRepository.count(spec)
            );
        });
    }

    @Override
    public ScrollResponse<Operator> findAll(@NonNull OperatorCondition condition, @NonNull ScrollCondition scrollCondition) {
        return readOnlyTransactionTemplate.execute(status -> {
            Specification<Operator> spec = OperatorSpecification.byCondition(condition);
            return new ScrollResponse<>(
                    operatorRepository.findAll(spec, scrollCondition, OperatorFetchProfile::all),
                    scrollCondition
            );
        });
    }

    @Override
    public Optional<Operator> getById(@NonNull Long aLong) {
        return Optional.empty();
    }

    @Override
    public Operator create(@NonNull Operator entity) {
        return transactionTemplate.execute(status -> {
            validate(entity);
            prepareToCreate(entity);
            return operatorRepository.save(entity);
        });
    }

    @Override
    public Optional<Operator> update(@NonNull Operator entity) {
        throw new NotImplementedException();
    }

    @Override
    public boolean delete(Long aLong) {
        throw new NotImplementedException();
    }

    private void prepareToCreate(Operator entity) {
        entity.setLanguages(entity.getLanguages().stream().map(entityLoader::load).toList());
    }

    private void validate(Operator entity) {

    }
}
