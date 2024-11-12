package ru.draen.hps.file.controller.mapper;

import ru.draen.hps.common.core.annotation.Mapper;
import ru.draen.hps.common.jpadao.entity.IEntity;
import ru.draen.hps.common.core.mapper.IMapper;
import ru.draen.hps.common.dbms.domain.File;
import ru.draen.hps.common.dbms.domain.FileContent;
import ru.draen.hps.common.dbms.domain.Operator;
import ru.draen.hps.file.controller.dto.FileContentDto;
import ru.draen.hps.file.controller.dto.FileDto;
import ru.draen.hps.file.controller.dto.OperatorBriefDto;

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
