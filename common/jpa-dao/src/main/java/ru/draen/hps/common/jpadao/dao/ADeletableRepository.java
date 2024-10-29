package ru.draen.hps.common.jpadao.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.common.jpadao.entity.ADeletableEntity;
import ru.draen.hps.common.jpadao.entity.ADeletableEntity_;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class ADeletableRepository<E extends ADeletableEntity<ID>, ID> extends AGenericRepository<E, ID>
        implements IDeletableRepository<E, ID> {

    @Override
    public Optional<E> findById(@NonNull ID id, @NonNull Consumer<Root<E>> fetchProfile) {
        return findOne((root, cq, cb) -> cb.and(
                cb.equal(root.get(getIdAttribute(root)), id),
                cb.isNull(root.get(ADeletableEntity_.delDate))
        ), fetchProfile);
    }

    @Override
    public boolean exists(@NonNull Specification<E> spec) {
        return super.exists(spec.and((root, cq, cb) -> cb.isNull(root.get(ADeletableEntity_.delDate))));
    }

    @Override
    public void delete(E entity) {
        entity.setDelDate(getServerTimestamp());
        entity.setDelUser("Anonymous user"); //TODO user session
        super.save(entity);
    }

    @Override
    public boolean delete(@NonNull Specification<E> spec) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<E> cu = cb.createCriteriaUpdate(getEntityClass());
        Root<E> root = cu.from(getEntityClass());

        cu.set(ADeletableEntity_.delDate, getServerTimestamp());
        cu.set(ADeletableEntity_.delUser, "Anonymous user"); //TODO user session

        cu.where(
                spec.toPredicate(root, cb.createQuery(), cb),
                cb.isNull(root.get(ADeletableEntity_.delDate))
        );

        return entityManager.createQuery(cu).executeUpdate() > 0;
    }

    @Override
    public boolean delete(@NonNull ID id) {
        return delete((root, cq, cb) -> cb.and(
                cb.equal(root.get(getIdAttribute(root)), id),
                cb.isNull(root.get(ADeletableEntity_.delDate))
        ));
    }
}
