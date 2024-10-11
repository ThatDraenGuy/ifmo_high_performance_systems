package ru.draen.hps.app.tariffrule.controller.mapper;

import ru.draen.hps.app.operator.controller.dto.OperatorBriefDto;
import ru.draen.hps.app.tariffrule.controller.dto.TariffRuleDto;
import ru.draen.hps.common.annotation.Mapper;
import ru.draen.hps.common.entity.IEntity;
import ru.draen.hps.common.mapper.IMapper;
import ru.draen.hps.domain.Operator;
import ru.draen.hps.domain.TariffRule;

@Mapper
public class TariffRuleMapper implements IMapper<TariffRule, TariffRuleDto> {
    @Override
    public TariffRule toEntity(TariffRuleDto dto) {
        TariffRule entity = new TariffRule();
        entity.setId(dto.getRuleId());
        entity.setOperator(IEntity.mapId(dto.getOperator().getOperatorId(), Operator::new));
        entity.setName(dto.getName());
        entity.setMinuteCost(dto.getMinuteCost());
        entity.setMinuteLimit(dto.getMinuteLimit());
        return entity;
    }

    @Override
    public TariffRuleDto toDto(TariffRule entity) {
        TariffRuleDto dto = new TariffRuleDto();
        dto.setRuleId(entity.getId());
        dto.setOperator(OperatorBriefDto.of(entity.getOperator()));
        dto.setName(entity.getName());
        dto.setMinuteCost(entity.getMinuteCost());
        dto.setMinuteLimit(entity.getMinuteLimit());
        return dto;
    }

}
