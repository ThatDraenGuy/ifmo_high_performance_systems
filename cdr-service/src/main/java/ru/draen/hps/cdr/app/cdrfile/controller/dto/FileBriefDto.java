package ru.draen.hps.cdr.app.cdrfile.controller.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.draen.hps.cdr.common.model.FileModel;

import static java.util.Objects.isNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileBriefDto {
    private Long fileId;
    private String fileName;
    private Long operatorId;

    public static FileBriefDto of(@Nullable FileModel model) {
        if (isNull(model)) return null;
        return new FileBriefDto(model.getFileId(), model.getFileName(), model.getOperator().getOperatorId());
    }
}
