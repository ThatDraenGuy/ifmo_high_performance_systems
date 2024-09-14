package ru.draen.hps.common.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.common.entity.IEntity;
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.ScrollCondition;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class AGenericRepository<E extends IEntity<ID>, ID> implements ISearchRepository<E, ID> {
    @PersistenceContext
    protected EntityManager entityManager;

    protected abstract Class<E> getEntityClass();
    protected abstract void defaultFetchProfile(Root<E> root);

    @Override
    public Optional<E> findById(@NonNull ID id) {
        return Optional.ofNullable(entityManager.find(getEntityClass(), id));
    }

    @Override
    public Optional<E> findOne(@NonNull Specification<E> spec) {
        return findOne(spec, this::defaultFetchProfile);
    }

    @Override
    public Optional<E> findOne(@NonNull Specification<E> spec, @NonNull Consumer<Root<E>> fetchProfile) {
        TypedQuery<E> query = getTypedQuery(spec, Sort.unsorted(), fetchProfile);
        return getSingleResult(query);
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
        return query.getResultList();
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
        return query.getResultList();
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

    protected TypedQuery<E> getTypedQuery(Specification<E> spec, Sort sort, Consumer<Root<E>> fetchProfile) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> cq = cb.createQuery(getEntityClass());
        Root<E> root = cq.from(getEntityClass());
        fetchProfile.accept(root);

        if (spec != null) {
            cq.where(spec.toPredicate(root, cq, cb));
        }
//        cq.orderBy(); //TODO
        return entityManager.createQuery(cq);
    }

    protected<X> Optional<X> getSingleResult(TypedQuery<X> query) {
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
