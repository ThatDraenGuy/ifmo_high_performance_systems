package ru.draen.hps.common.core.mapper;

import java.util.List;

public interface IMapper<E, D> {
    E toEntity(D dto);
    D toDto(E entity);

    default List<E> toEntity(List<D> dtos) {
        return dtos.stream().map(this::toEntity).toList();
    }
    default List<D> toDto(List<E> entities) {
        return entities.stream().map(this::toDto).toList();
    }
}
