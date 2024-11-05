package ru.draen.hps.account.app.operator.controller.mapper;


import ru.draen.hps.account.app.language.controller.dto.LanguageDto;
import ru.draen.hps.account.app.operator.controller.dto.OperatorDto;
import ru.draen.hps.common.core.annotation.Mapper;
import ru.draen.hps.common.core.mapper.IMapper;
import ru.draen.hps.common.dbms.domain.Language;
import ru.draen.hps.common.dbms.domain.Operator;
import ru.draen.hps.common.jpadao.entity.IEntity;

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
