package ru.draen.hps.app.file.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.draen.hps.domain.FileContent;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileContentDto {
    @NotNull
    private Long fileContentId;

    @NotEmpty
    private byte[] data;

    public static FileContentDto of(FileContent fileContent) {
        return new FileContentDto(fileContent.getId(), fileContent.getData());
    }
}
