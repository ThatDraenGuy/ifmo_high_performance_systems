package ru.draen.hps.cdr.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileContentModel {
    private Long fileContentId;
    private byte[] data;
}
