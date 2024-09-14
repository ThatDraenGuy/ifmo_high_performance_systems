package ru.draen.hps.common.model;

import org.springframework.data.domain.Sort;

public record ScrollCondition(int offset, int limit, Sort sort) {
}
