package ru.draen.hps.file.controller.mapper;

import ru.draen.hps.common.core.annotation.Mapper;
import ru.draen.hps.common.core.exception.NotImplementedException;
import ru.draen.hps.common.core.mapper.IMapper;
import ru.draen.hps.common.dbms.domain.File;
import ru.draen.hps.file.controller.dto.FileBriefDto;

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

}
