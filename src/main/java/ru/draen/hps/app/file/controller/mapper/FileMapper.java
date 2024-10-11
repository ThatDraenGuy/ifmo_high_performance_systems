package ru.draen.hps.app.file.controller.mapper;

import ru.draen.hps.app.file.controller.dto.FileContentDto;
import ru.draen.hps.app.file.controller.dto.FileDto;
import ru.draen.hps.app.operator.controller.dto.OperatorBriefDto;
import ru.draen.hps.common.annotation.Mapper;
import ru.draen.hps.common.entity.IEntity;
import ru.draen.hps.common.mapper.IMapper;
import ru.draen.hps.domain.File;
import ru.draen.hps.domain.FileContent;
import ru.draen.hps.domain.Operator;

@Mapper
public class FileMapper implements IMapper<File, FileDto> {
    @Override
    public File toEntity(FileDto dto) {
        File entity = new File();
        entity.setFileName(dto.getFileName());
        entity.setOperator(IEntity.mapId(dto.getOperator().getOperatorId(), Operator::new));

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
        dto.setOperator(OperatorBriefDto.of(entity.getOperator()));
        dto.setFileName(entity.getFileName());
        dto.setContent(FileContentDto.of(entity.getContent()));
        return dto;
    }

}
