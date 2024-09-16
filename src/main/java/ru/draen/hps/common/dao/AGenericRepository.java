package ru.draen.hps.common.dao;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.SingularAttribute;
import lombok.NonNull;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.common.entity.IEntity;
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.ScrollCondition;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Objects.isNull;


public abstract class AGenericRepository<E extends IEntity<ID>, ID> implements ICrudRepository<E, ID>, ISearchRepository<E, ID> {
    @PersistenceContext
    protected EntityManager entityManager;

    /**
     * Must return class of entity. Used for criteria API queries
     * @return Class of entity
     */
    protected abstract @NonNull Class<E> getEntityClass();

    /**
     * Defines default fetch profile for this entity.
     * Implementors should utilize {@link Root#fetch(SingularAttribute)} method (and its overloads) to fetch commonly needed attributes
     * @param root Root to add fetches onto
     */
    protected abstract void defaultFetchProfile(@NonNull Root<E> root);

    /**
     * Defines map of sorting attributes. When sorting, if an attribute is not found within implemented map,
     * an attempt to resolve path as a chain of entity fields will be performed
     * @return Map of explicitly defined sorting attributes. It may be an empty map.
     */
    protected abstract @NonNull Map<String, Path<?>> getExplicitSortingPaths();

    /**
     * Override this method to somehow alter result of find methods. Useful for fetching list/plural attributes
     * @param foundEntities List of entities that were found
     * @return List after your alterations. Can be the same instance as method argument
     */
    protected @NonNull List<E> modifyFindResult(@NonNull List<E> foundEntities) {
        return foundEntities;
    }

    @Override
    public Optional<E> findById(@NonNull ID id) {
        return findById(id, this::defaultFetchProfile);
    }

    @Override
    public Optional<E> findById(@NonNull ID id, @NonNull Consumer<Root<E>> fetchProfile) {
        return findOne((root, cq, cb) -> cb.equal(root, id), fetchProfile);
    }

    @Override
    public Optional<E> findOne(@NonNull Specification<E> spec) {
        return findOne(spec, this::defaultFetchProfile);
    }

    @Override
    public Optional<E> findOne(@NonNull Specification<E> spec, @NonNull Consumer<Root<E>> fetchProfile) {
        TypedQuery<E> query = getTypedQuery(spec, Sort.unsorted(), fetchProfile);
        return getSingleResult(query).flatMap(entity ->
                modifyFindResult(List.of(entity)).stream().findAny());
    }

    @Override
    public List<E> findAll(@NonNull Specification<E> spec, @NonNull ScrollCondition scrollCondition) {
        return findAll(spec, scrollCondition, this::defaultFetchProfile);
    }

    @Override
    public List<E> findAll(@NonNull Specification<E> spec, @NonNull ScrollCondition scrollCondition, @NonNull Consumer<Root<E>> fetchProfile) {
        TypedQuery<E> query = getTypedQuery(spec, scrollCondition.sort(), fetchProfile);
        query.setFirstResult(scrollCondition.offset());
        query.setMaxResults(scrollCondition.limit());
        return modifyFindResult(query.getResultList());
    }

    @Override
    public List<E> findAll(@NonNull Specification<E> spec, @NonNull PageCondition pageCondition) {
        return findAll(spec, pageCondition, this::defaultFetchProfile);
    }

    @Override
    public List<E> findAll(@NonNull Specification<E> spec, @NonNull PageCondition pageCondition, @NonNull Consumer<Root<E>> fetchProfile) {
        TypedQuery<E> query = getTypedQuery(spec, pageCondition.sort(), fetchProfile);
        query.setFirstResult(pageCondition.page() * pageCondition.size());
        query.setMaxResults(pageCondition.size());
        return modifyFindResult(query.getResultList());
    }

    @Override
    public long count(@NonNull Specification<E> spec) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<E> root = cq.from(getEntityClass());

        cq.select(cb.count(root));
        cq.where(spec.toPredicate(root, cq, cb));

        return ObjectUtils.defaultIfNull(entityManager.createQuery(cq).getSingleResult(), 0L);
    }

    @Override
    public boolean exists(@NonNull Specification<E> spec) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Boolean> cq = cb.createQuery(Boolean.class);
        Root<E> root = cq.from(getEntityClass());
        cq.select(cb.literal(true));
        cq.where(spec.toPredicate(root, cq, cb));
        TypedQuery<Boolean> query = entityManager.createQuery(cq);
        query.setMaxResults(1);
        return getSingleResult(query).orElse(false);
    }

    @Override
    public E save(E entity) {
        if (isNull(entity.getId())) {
            entityManager.persist(entity);
            entityManager.flush();
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }

    @Override
    public void delete(E entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    @Override
    public boolean delete(@NonNull ID id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<E> cd = cb.createCriteriaDelete(getEntityClass());
        Root<E> root = cd.from(getEntityClass());
        cd.where(cb.equal(root, id));
        return entityManager.createQuery(cd).executeUpdate() > 0;
    }

    protected TypedQuery<E> getTypedQuery(Specification<E> spec, Sort sort, Consumer<Root<E>> fetchProfile) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> cq = cb.createQuery(getEntityClass());
        Root<E> root = cq.from(getEntityClass());
        fetchProfile.accept(root);

        if (spec != null) {
            cq.where(spec.toPredicate(root, cq, cb));
        }
        cq.orderBy(resolveSorts(root, cb, sort));
        return entityManager.createQuery(cq);
    }

    protected List<Order> resolveSorts(Root<E> root, CriteriaBuilder cb, Sort sort) {
        Map<String, Path<?>> sortingPaths = getExplicitSortingPaths();
        return sort.get().map(order -> {
            Path<?> sortPath = sortingPaths.get(order.getProperty());
            if (isNull(sortPath)) {
                sortPath = root;
                for (String step : order.getProperty().split("\\.")) {
                    sortPath = sortPath.get(step);
                }
            }
            return order.getDirection() == Sort.Direction.ASC ? cb.asc(sortPath) : cb.desc(sortPath);
        }).toList();
    }

    protected<X> Optional<X> getSingleResult(TypedQuery<X> query) {
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    protected Instant getServerTimestamp() {
        Query query = entityManager.createNativeQuery("select current_timestamp as date_value");
        return ((Timestamp) query.getSingleResult()).toInstant();
    }
}
