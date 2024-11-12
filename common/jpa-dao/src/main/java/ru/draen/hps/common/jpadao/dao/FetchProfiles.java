package ru.draen.hps.common.jpadao.dao;

import jakarta.persistence.criteria.From;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FetchProfiles {
    public static <T> void nothing(From<?, T> root) {}
}
