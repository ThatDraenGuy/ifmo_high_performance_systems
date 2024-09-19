package ru.draen.hps.common.service;

import lombok.NonNull;
import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.PageResponse;
import ru.draen.hps.common.model.ScrollCondition;
import ru.draen.hps.common.model.ScrollResponse;

public interface ISearchService<E, C> {
    PageResponse<E> findAll(@NonNull C condition, @NonNull PageCondition pageCondition);
    ScrollResponse<E> findAll(@NonNull C condition, @NonNull ScrollCondition scrollCondition);
}
