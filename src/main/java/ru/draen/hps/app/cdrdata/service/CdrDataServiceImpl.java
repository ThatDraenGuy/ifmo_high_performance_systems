package ru.draen.hps.app.cdrdata.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.draen.hps.app.cdrdata.controller.dto.CdrDataCondition;
import ru.draen.hps.app.cdrdata.dao.CdrDataRepository;
import ru.draen.hps.app.cdrdata.dao.CdrDataSpecification;
import ru.draen.hps.common.model.*;
import ru.draen.hps.domain.CdrData;

import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class CdrDataServiceImpl implements CdrDataService {
    private final TransactionTemplate readOnlyTransactionTemplate;
    private final TransactionTemplate transactionTemplate;
    private final CdrDataRepository cdrDataRepository;

    @Override
    public PageResponse<CdrData> findAll(@NonNull CdrDataCondition condition, @NonNull PageCondition pageCondition) {
        return readOnlyTransactionTemplate.execute(status -> {
            Specification<CdrData> spec = CdrDataSpecification.byCondition(condition);
            return new PageResponse<>(
                    cdrDataRepository.findAll(spec, pageCondition),
                    pageCondition,
                    cdrDataRepository.count(spec)
            );
        });
    }

    @Override
    public ScrollResponse<CdrData> findAll(@NonNull CdrDataCondition condition, @NonNull ScrollCondition scrollCondition) {
        return readOnlyTransactionTemplate.execute(status -> {
            Specification<CdrData> spec = CdrDataSpecification.byCondition(condition);
            return new ScrollResponse<>(
                    cdrDataRepository.findAll(spec, scrollCondition),
                    scrollCondition
            );
        });
    }

    @Override
    public Stream<CdrData> findStream(@NonNull Specification<CdrData> spec, @NonNull StreamCondition streamCondition) {
        return readOnlyTransactionTemplate.execute(status -> cdrDataRepository.findStream(spec, streamCondition));
    }

    @Override
    public CdrData save(CdrData cdrData) {
        return transactionTemplate.execute(status -> cdrDataRepository.save(cdrData));
    }
}
