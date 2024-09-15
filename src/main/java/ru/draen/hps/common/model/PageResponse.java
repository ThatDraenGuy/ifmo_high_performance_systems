package ru.draen.hps.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NonNull;

import java.util.List;
import java.util.function.Function;

public record PageResponse<T>(@NonNull ListInfo listInfo, @NonNull List<T> items) {
    public PageResponse(@NonNull List<T> items, @NonNull PageCondition pageCondition, Long count) {
        this(new ListInfo(pageCondition.page(), pageCondition.size(), count), items);
    }

    public <R> PageResponse<R> map(@NonNull Function<T, R> converter) {
        return new PageResponse<>(listInfo, items.stream().map(converter).toList());
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public record ListInfo(int page, int size, Long count) {
    }
}
