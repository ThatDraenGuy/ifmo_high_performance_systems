package ru.draen.hps.app.cdrfile.dao;

import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.dao.ADeletableRepository;
import ru.draen.hps.domain.CdrFile;
import ru.draen.hps.domain.CdrFile_;

@Repository
public class CdrFileRepositoryImpl extends ADeletableRepository<CdrFile, Long> implements CdrFileRepository {
    @Override
    protected @NonNull Class<CdrFile> getEntityClass() {
        return CdrFile.class;
    }

    @Override
    protected void defaultFetchProfile(@NonNull Root<CdrFile> root) {
        root.fetch(CdrFile_.file);
    }
}
