package ru.draen.hps.common.service;

import lombok.NonNull;
import ru.draen.hps.common.entity.IEntity;

import java.util.Optional;

public interface ICrudService<E extends IEntity<ID>, ID> {
    Optional<E> getById(@NonNull ID id);
    E create(@NonNull E entity);
    Optional<E> update(@NonNull E entity);
    boolean delete(ID id);
}
