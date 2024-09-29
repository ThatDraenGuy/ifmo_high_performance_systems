package ru.draen.hps.app.cdrfile.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.draen.hps.app.file.controller.dto.FileBriefDto;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CdrFileDto {
    private Long fileId;
    private FileBriefDto file;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
}
