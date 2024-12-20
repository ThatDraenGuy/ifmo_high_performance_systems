package ru.draen.hps.account.app.operator.dao;

import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.dbms.domain.Operator;
import ru.draen.hps.common.jpadao.dao.ADeletableRepository;
import ru.draen.hps.common.jpadao.dao.FetchProfiles;

@Repository
public class OperatorRepositoryImpl extends ADeletableRepository<Operator, Long> implements OperatorRepository {
    @Override
    protected @NonNull Class<Operator> getEntityClass() {
        return Operator.class;
    }

    @Override
    protected void defaultFetchProfile(@NonNull Root<Operator> root) {
        FetchProfiles.nothing(root);
    }
}
