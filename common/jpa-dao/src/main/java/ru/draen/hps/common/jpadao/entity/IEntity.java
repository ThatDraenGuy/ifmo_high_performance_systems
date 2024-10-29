package ru.draen.hps.common.jpadao.entity;

import lombok.NonNull;

import java.util.function.Supplier;

public interface IEntity<ID> {
    ID getId();
    void setId(ID id);

    static<ID, E extends IEntity<ID>> E mapId(ID id, @NonNull Supplier<E> entityProvider) {
        if (id == null) {
            return null;
        }
        E entity = entityProvider.get();
        entity.setId(id);
        return entity;
    }
}
