package ru.draen.hps.common.dao;

import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.common.entity.AHistoricalEntity;

import java.util.Collection;

public interface IHistoricalRepository<E extends AHistoricalEntity<ID>, ID> extends IDeletableRepository<E, ID> {
    boolean exists(@NonNull E entity);
    Specification<E> logicalKey(@NonNull E entity);
    ID saveOrUpdate(@NonNull E entity);
    void saveOrUpdate(@NonNull Collection<E> entities);
}
