package ru.draen.hps.common.r2dbcdao.dao;

import lombok.NonNull;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.r2dbcdao.entity.IEntity;

public interface ICrudRepository<E extends IEntity<ID>, ID> {
    Mono<E> findById(@NonNull ID id);
    Mono<E> save(@NonNull E entity);
    Mono<Void> delete(@NonNull E entity);
    Mono<Boolean> delete(@NonNull ID id);
}
