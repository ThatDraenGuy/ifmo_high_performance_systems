package ru.draen.hps.common.dao;

import lombok.NonNull;
import ru.draen.hps.common.entity.IEntity;

import java.util.Optional;

public interface ICrudRepository<E extends IEntity<ID>, ID> {
    Optional<E> findById(@NonNull ID id);
    E save(E entity);
    void delete(E entity);
    boolean delete(@NonNull ID id);
}
