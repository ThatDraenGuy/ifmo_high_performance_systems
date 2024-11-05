package ru.draen.hps.cdr.dto;

import lombok.Data;

@Data
public class FileDto {
    private Long fileId;
    private String fileName;
    private OperatorBriefDto operator;
    private FileContentDto content;
}