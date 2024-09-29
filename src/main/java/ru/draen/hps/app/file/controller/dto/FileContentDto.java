package ru.draen.hps.app.file.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.draen.hps.domain.FileContent;

import static java.util.Objects.isNull;

@Data
@AllArgsConstructor
public class FileContentDto {
    @NotNull
    private Long fileContentId;

    @NotEmpty
    private byte[] data;

    public static FileContentDto of(FileContent fileContent) {
        if (isNull(fileContent))
            return null;
        return new FileContentDto(fileContent.getId(), fileContent.getData());
    }
}
