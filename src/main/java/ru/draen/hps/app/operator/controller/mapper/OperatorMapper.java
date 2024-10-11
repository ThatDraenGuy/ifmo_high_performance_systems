package ru.draen.hps.app.operator.controller.mapper;

import ru.draen.hps.app.language.controller.dto.LanguageDto;
import ru.draen.hps.app.operator.controller.dto.OperatorDto;
import ru.draen.hps.common.annotation.Mapper;
import ru.draen.hps.common.entity.IEntity;
import ru.draen.hps.common.mapper.IMapper;
import ru.draen.hps.domain.Language;
import ru.draen.hps.domain.Operator;

@Mapper
public class OperatorMapper implements IMapper<Operator, OperatorDto> {
    @Override
    public Operator toEntity(OperatorDto dto) {
        Operator entity = new Operator();
        entity.setId(dto.getOperatorId());
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setLanguages(dto.getLanguages().stream()
                .map(lang -> IEntity.mapId(lang.getLanguageId(), Language::new)).toList());
        return entity;
    }

    @Override
    public OperatorDto toDto(Operator entity) {
        OperatorDto dto = new OperatorDto();
        dto.setOperatorId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setLanguages(entity.getLanguages().stream().map(LanguageDto::of).toList());
        return dto;
    }

}
