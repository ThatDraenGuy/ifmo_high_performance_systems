package ru.draen.hps.app.file.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.draen.hps.domain.File;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileBriefDto {
    private Long fileId;
    private String fileName;
    private Long operatorId;

    public static FileBriefDto of(File file) {
        return new FileBriefDto(file.getId(), file.getFileName(), file.getOperId());
    }
}
