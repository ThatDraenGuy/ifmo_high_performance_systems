package ru.draen.hps.common.jpadao.model;

import org.springframework.data.domain.Sort;

public record StreamCondition(int offset, Sort sort) {
}
