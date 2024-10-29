package ru.draen.hps.common.jpadao.dao;

import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.common.jpadao.entity.AHistoricalEntity;

public interface IHistoricalRepository<E extends AHistoricalEntity<ID>, ID> extends IDeletableRepository<E, ID> {
    boolean exists(@NonNull E entity);
    Specification<E> logicalKey(@NonNull E entity);
    ID saveOrUpdate(@NonNull E entity);
}
