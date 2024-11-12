package ru.draen.hps.common.jpadao.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NonNull;

import java.util.List;
import java.util.function.Function;

public record ScrollResponse<T>(@NonNull ListInfo listInfo, @NonNull List<T> items) {

    public ScrollResponse(@NonNull List<T> items, @NonNull ScrollCondition scrollCondition) {
        this(new ListInfo(scrollCondition.limit(), scrollCondition.offset()), items);
    }

    public <R> ScrollResponse<R> map(@NonNull Function<T, R> converter) {
        return new ScrollResponse<>(listInfo, items.stream().map(converter).toList());
    }

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private record ListInfo(int limit, long offset) {
    }
}
