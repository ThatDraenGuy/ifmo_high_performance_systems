package ru.draen.hps.cdr.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileModel {
    private Long fileId;
    private String fileName;
    private OperatorBriefModel operator;
    private FileContentModel content;
}