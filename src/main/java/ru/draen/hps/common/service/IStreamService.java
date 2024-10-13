package ru.draen.hps.common.service;

import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.common.model.StreamCondition;

import java.util.stream.Stream;

public interface IStreamService<E> {
    Stream<E> findStream(@NonNull Specification<E> spec, @NonNull StreamCondition streamCondition);
}
