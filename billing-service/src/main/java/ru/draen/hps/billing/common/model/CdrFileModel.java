package ru.draen.hps.billing.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CdrFileModel {
    private Long fileId;
    private FileBriefModel file;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
}
