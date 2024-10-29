package ru.draen.hps.common.r2dbcdao.dao;

import lombok.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.r2dbcdao.entity.IEntity;
import ru.draen.hps.common.r2dbcdao.model.SearchCondition;
import ru.draen.hps.common.r2dbcdao.model.Specification;

public interface ISearchRepository<E extends IEntity<ID>, ID> {
    Mono<E> findById(@NonNull ID id);
    Mono<E> findOne(@NonNull Specification spec);
    Flux<E> findAll(@NonNull Specification spec);
    Flux<E> findAll(@NonNull Specification spec, @NonNull SearchCondition condition);
    Mono<Long> count(@NonNull Specification spec);
    Mono<Boolean> exists(@NonNull Specification spec);
}
