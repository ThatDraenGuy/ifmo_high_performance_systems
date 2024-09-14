package ru.draen.hps.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.function.Function;

public record PageResponse<T>(ListInfo listInfo, List<T> items) {
    public PageResponse(List<T> items, PageCondition pageCondition, Long count) {
        this(new ListInfo(pageCondition.page(), pageCondition.size(), count), items);
    }

    public <R> PageResponse<R> map(Function<T, R> converter) {
        return new PageResponse<>(listInfo, items.stream().map(converter).toList());
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private record ListInfo(int page, int size, Long count) {
    }
}
