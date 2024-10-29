package ru.draen.hps.common.jpadao.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum EHistStatus {
    @JsonProperty("Active") ACTIVE,
    @JsonProperty("Future") FUTURE,
    @JsonProperty("Obsolete") OBSOLETE,
    @JsonProperty("Deleted") DELETED;

    @JsonCreator
    public static EHistStatus valueOfIgnoreCase(String status) {
        return EHistStatus.valueOf(status.trim().toUpperCase());
    }
}
