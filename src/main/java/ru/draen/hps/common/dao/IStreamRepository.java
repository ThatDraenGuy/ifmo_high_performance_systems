package ru.draen.hps.common.dao;

import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.common.entity.IEntity;
import ru.draen.hps.common.model.StreamCondition;

import java.util.function.Consumer;
import java.util.stream.Stream;

public interface IStreamRepository<E extends IEntity<ID>, ID> {
    Stream<E> findStream(@NonNull Specification<E> spec);
    Stream<E> findStream(@NonNull Specification<E> spec, @NonNull StreamCondition condition);
    Stream<E> findStream(@NonNull Specification<E> spec, @NonNull StreamCondition condition, @NonNull Consumer<Root<E>> fetchProfile);
}
