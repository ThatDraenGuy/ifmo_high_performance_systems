package ru.draen.hps.common.r2dbcdao.dao;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.draen.hps.common.r2dbcdao.entity.IEntity;
import ru.draen.hps.common.r2dbcdao.model.SearchCondition;
import ru.draen.hps.common.r2dbcdao.model.Specification;

import static java.util.Objects.isNull;


//@AllArgsConstructor
public abstract class AGenericRepository<E extends IEntity<ID>, ID> implements ISearchRepository<E, ID>, ICrudRepository<E, ID> {
    private final R2dbcEntityOperations entityOperations;
    private final RelationalPersistentProperty idProperty;

    public AGenericRepository(R2dbcEntityOperations entityOperations, R2dbcConverter converter) {
        this.entityOperations = entityOperations;
        idProperty = converter.getMappingContext().getRequiredPersistentEntity(getEntityClass()).getRequiredIdProperty();
    }

    protected abstract @NonNull Class<E> getEntityClass();

    @Override
    public Mono<E> findById(@NonNull ID id) {
        return entityOperations.selectOne(getIdQuery(id), getEntityClass());
    }

    @Override
    public Mono<E> save(@NonNull E entity) {
        if (isNull(entity.getId())) {
            return entityOperations.insert(entity);
        } else {
            return entityOperations.update(entity);
        }
    }

    @Override
    public Mono<Void> delete(@NonNull E entity) {
        return entityOperations.delete(entity).then();
    }

    @Override
    public Mono<Boolean> delete(@NonNull ID id) {
        return entityOperations.delete(getIdQuery(id), getEntityClass()).map(count -> count > 0);
    }

    @Override
    public Mono<E> findOne(@NonNull Specification spec) {
        Query query = getQuery(spec, Sort.unsorted());
        return entityOperations.selectOne(query, getEntityClass());
    }

    public Flux<E> findAll(@NonNull Specification spec) {
        Query query = getQuery(spec, Sort.unsorted());
        return entityOperations.select(query, getEntityClass());
    }

    @Override
    public Flux<E> findAll(@NonNull Specification spec, @NonNull SearchCondition condition) {
        Query query = getQuery(spec, condition.sort())
                .offset(condition.offset());
        return entityOperations.select(query, getEntityClass());
    }

    @Override
    public Mono<Long> count(@NonNull Specification spec) {
        Query query = getQuery(spec, Sort.unsorted());
        return entityOperations.count(query, getEntityClass());
    }

    @Override
    public Mono<Boolean> exists(@NonNull Specification spec) {
        Query query = getQuery(spec, Sort.unsorted());
        return entityOperations.exists(query, getEntityClass());
    }

    protected Query getIdQuery(Object id) {
        return Query.query(Criteria.where(idProperty.getName()).is(id));
    }

    protected Query getQuery(Specification spec, Sort sort) {
        Query query = Query.query(spec.toPredicate());
        query.sort(sort);
        return query;
    }
}
