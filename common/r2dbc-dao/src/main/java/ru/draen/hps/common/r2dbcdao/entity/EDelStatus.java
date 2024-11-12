package ru.draen.hps.common.r2dbcdao.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum EDelStatus {
    @JsonProperty("Active") ACTIVE,
    @JsonProperty("Deleted") DELETED;

    @JsonCreator
    public static EDelStatus valueOfIgnoreCase(String status) {
        return EDelStatus.valueOf(status.trim().toUpperCase());
    }
}
