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
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.PageResponse;
import ru.draen.hps.common.model.ScrollCondition;
import ru.draen.hps.common.model.ScrollResponse;
import ru.draen.hps.domain.Operator;

@Service
@AllArgsConstructor
public class OperatorServiceImpl implements OperatorService {
    private final TransactionTemplate readOnlyTransactionTemplate;
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
}
