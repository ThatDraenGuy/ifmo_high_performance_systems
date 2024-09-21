package ru.draen.hps.app.file.controller.mapper;

import ru.draen.hps.app.file.controller.dto.FileBriefDto;
import ru.draen.hps.common.annotation.Mapper;
import ru.draen.hps.common.exception.NotImplementedException;
import ru.draen.hps.common.mapper.IMapper;
import ru.draen.hps.domain.File;

@Mapper
public class FileBriefMapper implements IMapper<File, FileBriefDto> {
    @Override
    public File toEntity(FileBriefDto dto) {
        throw new NotImplementedException();
    }

    @Override
    public FileBriefDto toDto(File entity) {
        return FileBriefDto.of(entity);
    }

    @Override
    public FileBriefDto toId(File entity) {
        FileBriefDto dto = new FileBriefDto();
        dto.setFileId(entity.getId());
        return dto;
    }
}
