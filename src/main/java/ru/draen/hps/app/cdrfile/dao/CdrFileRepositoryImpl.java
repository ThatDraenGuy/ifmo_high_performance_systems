package ru.draen.hps.app.cdrfile.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.dao.ADeletableRepository;
import ru.draen.hps.domain.*;

import java.util.stream.Stream;

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

    @Override
    public Stream<Client> getClients(CdrFile entity) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> cq = cb.createQuery(Client.class);
        Root<CdrData> root = cq.from(CdrData.class);

        cq.select(root.get(CdrData_.client)).distinct(true);

        cq.where(cb.and(
                cb.equal(root.get(CdrData_.cdrFile), entity),
                cb.isNull(root.get(CdrData_.delDate))
        ));

        return entityManager.createQuery(cq).getResultStream();
    }
}
