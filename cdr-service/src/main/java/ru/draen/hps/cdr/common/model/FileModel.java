package ru.draen.hps.cdr.common.model;

import lombok.Data;

@Data
public class FileModel {
    private Long fileId;
    private String fileName;
    private OperatorBriefModel operator;
    private FileContentModel content;
}