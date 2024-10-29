package ru.draen.hps.common.jpadao.model;

import org.springframework.data.domain.Sort;

public record ScrollCondition(int offset, int limit, Sort sort) {
}
