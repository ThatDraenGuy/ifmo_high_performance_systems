package ru.draen.hps.common.service;

import ru.draen.hps.common.model.PageCondition;
import ru.draen.hps.common.model.PageResponse;
import ru.draen.hps.common.model.ScrollCondition;
import ru.draen.hps.common.model.ScrollResponse;

public interface ISearchService<E, C> {
    PageResponse<E> findAll(C condition, PageCondition pageCondition);
    ScrollResponse<E> findAll(C condition, ScrollCondition scrollCondition);
}
