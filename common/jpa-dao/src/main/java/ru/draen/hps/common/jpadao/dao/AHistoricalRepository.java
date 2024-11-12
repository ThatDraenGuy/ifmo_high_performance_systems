package ru.draen.hps.common.jpadao.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.common.jpadao.entity.AHistoricalEntity;
import ru.draen.hps.common.jpadao.entity.AHistoricalEntity_;

import java.util.Optional;

public abstract class AHistoricalRepository<E extends AHistoricalEntity<ID>, ID>
        extends ADeletableRepository<E, ID> implements IHistoricalRepository<E, ID> {

    @Override
    public boolean exists(@NonNull E entity) {
        return exists(logicalKey(entity));
    }

    @Override
    public boolean exists(@NonNull Specification<E> spec) {
        return super.exists(
                spec.and(
                        (root, cq, cb) -> cb.lessThan(root.get(AHistoricalEntity_.startDate), root.get(AHistoricalEntity_.endDate))
                )
        );
    }

    @Override
    public ID saveOrUpdate(@NonNull E entity) {
        close(entity);
        entity.setId(null);
        super.save(entity);
        return entity.getId();
    }

    protected boolean close(E entity) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<E> cu = cb.createCriteriaUpdate(getEntityClass());
        Root<E> root = cu.from(getEntityClass());

        cu.set(AHistoricalEntity_.endDate, entity.getStartDate());

        cu.where(
                logicalKey(entity).toPredicate(root, cb.createQuery(), cb),
                cb.greaterThan(root.get(AHistoricalEntity_.endDate), entity.getStartDate()),
                cb.lessThan(root.get(AHistoricalEntity_.startDate), root.get(AHistoricalEntity_.endDate)),
                cb.isNull(root.get(AHistoricalEntity_.delDate))
        );

        return entityManager.createQuery(cu).executeUpdate() != 0;
    }

    @Override
    public boolean delete(@NonNull Specification<E> spec) {
        return super.delete(
                spec.and(
                        (root, cq, cb) -> cb.lessThan(root.get(AHistoricalEntity_.startDate), root.get(AHistoricalEntity_.endDate))
                ));
    }

    @Override
    public boolean delete(@NonNull ID id) {
        Optional<E> entity = findOne((root, cq, cb) -> cb.and(
                cb.equal(root.get(getIdAttribute(root)), id),
                cb.lessThan(root.get(AHistoricalEntity_.startDate), root.get(AHistoricalEntity_.endDate)),
                cb.isNull(root.get(AHistoricalEntity_.delDate))
        ));
        entity.ifPresent(this::delete);
        return entity.isPresent();
    }

    @Override
    public void delete(E entity) {
        this.delete(logicalKey(entity));
    }
}
