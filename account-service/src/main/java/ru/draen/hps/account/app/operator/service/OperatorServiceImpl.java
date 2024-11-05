package ru.draen.hps.account.app.operator.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.draen.hps.account.app.operator.dao.OperatorFetchProfile;
import ru.draen.hps.account.app.operator.dao.OperatorRepository;
import ru.draen.hps.common.dbms.domain.Operator;

import java.util.Optional;

@Service
@AllArgsConstructor
public class OperatorServiceImpl implements OperatorService {
    private final TransactionTemplate readOnlyTransactionTemplate;
    private final OperatorRepository operatorRepository;

    @Override
    public Optional<Operator> getById(@NonNull Long id) {
        return readOnlyTransactionTemplate.execute(status -> operatorRepository.findById(id, OperatorFetchProfile::all));
    }
}
