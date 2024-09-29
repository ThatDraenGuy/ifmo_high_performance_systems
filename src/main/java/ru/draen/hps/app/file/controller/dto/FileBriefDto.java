package ru.draen.hps.app.file.controller.dto;

import lombok.*;
import ru.draen.hps.domain.File;

import static java.util.Objects.isNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileBriefDto {
    private Long fileId;
    private String fileName;
    private Long operatorId;

    public static FileBriefDto of(File file) {
        if (isNull(file))
            return null;
        return new FileBriefDto(file.getId(), file.getFileName(), file.getOperator().getId());
    }
}
