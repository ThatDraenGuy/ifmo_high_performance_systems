package ru.draen.hps.app.file.controller.dto;

import lombok.Data;

@Data
public class FileCondition {
    private Long fileId;
    private Long operatorId;
    private String fileName;
}
