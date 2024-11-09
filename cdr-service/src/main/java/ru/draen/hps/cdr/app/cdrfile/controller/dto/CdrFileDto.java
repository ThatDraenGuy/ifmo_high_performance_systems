package ru.draen.hps.cdr.app.cdrfile.controller.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.draen.hps.cdr.common.model.FileModel;
import ru.draen.hps.common.core.utils.TimestampHelper;
import ru.draen.hps.common.r2dbcdao.domain.CdrFile;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CdrFileDto {
    private Long fileId;
    private FileBriefDto file;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;

    public static CdrFileDto of(@Nullable CdrFile entity, @Nullable FileModel model) {
        if (entity == null) return null;
        return new CdrFileDto(entity.getId(), FileBriefDto.of(model),
                TimestampHelper.atOffset(entity.getStartTime()), TimestampHelper.atOffset(entity.getEndTime()));
    }
}
