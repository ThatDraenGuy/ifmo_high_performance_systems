package ru.draen.hps.common.model;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public record PageCondition(int page, int size, Sort sort) {
    public PageRequest toPageRequest() {
        return PageRequest.of(page, size, sort);
    }
}
