package ru.draen.hps.app.cdrfile.controller.mapper;

import ru.draen.hps.app.cdrfile.controller.dto.CdrFileDto;
import ru.draen.hps.app.file.controller.dto.FileBriefDto;
import ru.draen.hps.common.annotation.Mapper;
import ru.draen.hps.common.exception.NotImplementedException;
import ru.draen.hps.common.mapper.IMapper;
import ru.draen.hps.common.utils.TimestampHelper;
import ru.draen.hps.domain.CdrFile;

@Mapper
public class CdrFileMapper implements IMapper<CdrFile, CdrFileDto> {
    @Override
    public CdrFile toEntity(CdrFileDto dto) {
        throw new NotImplementedException();
    }

    @Override
    public CdrFileDto toDto(CdrFile entity) {
        CdrFileDto dto = new CdrFileDto();
        dto.setFile(FileBriefDto.of(entity.getFile()));
        dto.setFileId(entity.getId());
        dto.setStartDate(TimestampHelper.atOffset(entity.getStartTime()));
        dto.setEndDate(TimestampHelper.atOffset(entity.getEndTime()));
        return dto;
    }

}
