package ru.draen.hps.billing.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileBriefModel {
    private Long fileId;
    private String fileName;
    private Long operatorId;
}
