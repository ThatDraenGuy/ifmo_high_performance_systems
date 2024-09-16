package ru.draen.hps.common.dao;

import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.common.entity.IEntity;
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.ScrollCondition;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface ISearchRepository<E extends IEntity<ID>, ID> {
    Optional<E> findById(@NonNull ID id);
    Optional<E> findById(@NonNull ID id, @NonNull Consumer<Root<E>> fetchProfile);
    Optional<E> findOne(@NonNull Specification<E> spec);
    Optional<E> findOne(@NonNull Specification<E> spec, @NonNull Consumer<Root<E>> fetchProfile);
    List<E> findAll(@NonNull Specification<E> spec, @NonNull ScrollCondition scrollCondition);
    List<E> findAll(@NonNull Specification<E> spec, @NonNull PageCondition pageCondition);
    List<E> findAll(@NonNull Specification<E> spec, @NonNull ScrollCondition scrollCondition, @NonNull Consumer<Root<E>> fetchProfile);
    List<E> findAll(@NonNull Specification<E> spec, @NonNull PageCondition pageCondition, @NonNull Consumer<Root<E>> fetchProfile);
    long count(@NonNull Specification<E> spec);
    boolean exists(@NonNull Specification<E> spec);
}
