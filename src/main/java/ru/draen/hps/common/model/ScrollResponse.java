package ru.draen.hps.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.function.Function;

public record ScrollResponse<T>(ListInfo listInfo, List<T> items) {

    public ScrollResponse(List<T> items, ScrollCondition scrollCondition) {
        this(new ListInfo(scrollCondition.limit(), scrollCondition.offset()), items);
    }

    public <R> ScrollResponse<R> map(Function<T, R> converter) {
        return new ScrollResponse<>(listInfo, items.stream().map(converter).toList());
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private record ListInfo(int limit, long offset) {
    }
}
