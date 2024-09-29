package ru.draen.hps.app.cdrdata.dao;

import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.dao.ADeletableRepository;
import ru.draen.hps.domain.CdrData;
import ru.draen.hps.domain.CdrData_;

@Repository
public class CdrDataRepositoryImpl extends ADeletableRepository<CdrData, Long> implements CdrDataRepository {
    @Override
    protected @NonNull Class<CdrData> getEntityClass() {
        return CdrData.class;
    }

    @Override
    protected void defaultFetchProfile(@NonNull Root<CdrData> root) {
        root.fetch(CdrData_.client);
        root.fetch(CdrData_.currency);
    }
}
