package ru.draen.hps.cdr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileContentDto {
    private Long fileContentId;
    private byte[] data;
}
