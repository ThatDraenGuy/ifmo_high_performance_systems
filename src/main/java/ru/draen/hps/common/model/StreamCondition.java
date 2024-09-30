package ru.draen.hps.common.model;

import org.springframework.data.domain.Sort;

public record StreamCondition(int offset, Sort sort) {
}
