package ru.draen.hps.common.dao;

import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.common.entity.ADeletableEntity;

public interface IDeletableRepository<E extends ADeletableEntity<ID>, ID> {
    boolean delete(@NonNull Specification<E> spec);
    boolean delete(@NonNull ID id);
}
