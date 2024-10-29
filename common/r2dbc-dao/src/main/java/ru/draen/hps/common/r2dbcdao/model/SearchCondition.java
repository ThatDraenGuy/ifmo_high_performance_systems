package ru.draen.hps.common.r2dbcdao.model;

import org.springframework.data.domain.Sort;

public record SearchCondition(int offset, Sort sort) {
}
