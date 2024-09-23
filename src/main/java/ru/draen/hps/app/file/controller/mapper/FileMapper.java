package ru.draen.hps.app.file.controller.mapper;

import ru.draen.hps.app.file.controller.dto.FileContentDto;
import ru.draen.hps.app.file.controller.dto.FileDto;
import ru.draen.hps.common.annotation.Mapper;
import ru.draen.hps.common.mapper.IMapper;
import ru.draen.hps.domain.File;
import ru.draen.hps.domain.FileContent;

@Mapper
public class FileMapper implements IMapper<File, FileDto> {
    @Override
    public File toEntity(FileDto dto) {
        File entity = new File();
        entity.setFileName(dto.getFileName());
        entity.setOperId(dto.getOperatorId());

        FileContent fileContent = new FileContent();
        fileContent.setData(dto.getContent().getData());
        fileContent.setFile(entity);
        entity.setContent(fileContent);
        return entity;
    }

    @Override
    public FileDto toDto(File entity) {
        FileDto dto = new FileDto();
        dto.setFileId(entity.getId());
        dto.setFileName(entity.getFileName());
        dto.setOperatorId(entity.getOperId());
        dto.setContent(FileContentDto.of(entity.getContent()));
        return dto;
    }

    @Override
    public FileDto toId(File entity) {
        FileDto dto = new FileDto();
        dto.setFileId(entity.getId());
        return dto;
    }
}
