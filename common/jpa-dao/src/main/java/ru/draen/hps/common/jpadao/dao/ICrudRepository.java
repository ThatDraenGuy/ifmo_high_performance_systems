package ru.draen.hps.common.jpadao.dao;

import lombok.NonNull;
import ru.draen.hps.common.jpadao.entity.IEntity;

import java.util.Optional;
import java.util.stream.Stream;

public interface ICrudRepository<E extends IEntity<ID>, ID> {
    Optional<E> findById(@NonNull ID id);
    E save(@NonNull E entity);
    void save(@NonNull Stream<E> entities);
    void delete(E entity);
    boolean delete(@NonNull ID id);
}
