package ru.draen.hps.app.file.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.*;
import ru.draen.hps.common.validation.groups.Create;
import ru.draen.hps.domain.FileContent;

import static java.util.Objects.isNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileContentDto {
    @Null(groups = Create.class)
    private Long fileContentId;

    @NotEmpty
    private byte[] data;

    public static FileContentDto of(FileContent fileContent) {
        if (isNull(fileContent))
            return null;
        return new FileContentDto(fileContent.getId(), fileContent.getData());
    }
}
