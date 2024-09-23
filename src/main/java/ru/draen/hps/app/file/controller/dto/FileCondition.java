package ru.draen.hps.app.file.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileCondition {
    private Long fileId;
    private Long operatorId;
    private String fileName;
}
