package ru.draen.hps.app.tariff.controller.mapper;

import lombok.AllArgsConstructor;
import ru.draen.hps.app.operator.controller.dto.OperatorDto;
import ru.draen.hps.app.tariff.controller.dto.AppliedTariffRuleDto;
import ru.draen.hps.app.tariff.controller.dto.TariffDto;
import ru.draen.hps.app.tariffrule.controller.dto.TariffRuleDto;
import ru.draen.hps.common.annotation.Mapper;
import ru.draen.hps.common.entity.IEntity;
import ru.draen.hps.common.mapper.IMapper;
import ru.draen.hps.common.utils.TimestampHelper;
import ru.draen.hps.domain.*;

import java.util.List;

@Mapper
@AllArgsConstructor
public class TariffMapper implements IMapper<TariffHist, TariffDto> {
    private final IMapper<TariffRule, TariffRuleDto> tariffRuleMapper;

    @Override
    public TariffHist toEntity(TariffDto dto) {
        TariffHist entity = new TariffHist();
        entity.setId(dto.getHistId());

        Tariff tariff = new Tariff();
        tariff.setId(dto.getTariffId());
        tariff.setName(dto.getName());
        tariff.setOperator(IEntity.mapId(dto.getOperator().getOperatorId(), Operator::new));

        entity.setTariff(tariff);
        entity.setRules(rulesToEntity(entity, dto.getRules()));
        entity.setStartDate(TimestampHelper.toInstant(dto.getStartDate()));
        entity.setEndDate(TimestampHelper.toInstant(dto.getEndDate()));
        return entity;
    }

    @Override
    public TariffDto toDto(TariffHist entity) {
        TariffDto dto = new TariffDto();
        dto.setHistId(entity.getId());
        dto.setTariffId(entity.getTariff().getId());
        dto.setName(entity.getTariff().getName());
        dto.setOperator(OperatorDto.of(entity.getTariff().getOperator()));
        dto.setRules(rulesToDto(entity.getRules()));
        dto.setStartDate(TimestampHelper.atOffset(entity.getStartDate()));
        dto.setEndDate(TimestampHelper.atOffset(entity.getEndDate()));
        dto.setStatus(entity.getHistStatus());
        return dto;
    }

    @Override
    public TariffDto toId(TariffHist entity) {
        TariffDto dto = new TariffDto();
        dto.setHistId(entity.getId());
        return dto;
    }

    private List<TariffToRules> rulesToEntity(TariffHist entity, List<AppliedTariffRuleDto> rules) {
        return rules.stream().map(rule -> {
            TariffToRules tariffToRules = new TariffToRules();
            tariffToRules.setTariffHist(entity);
            tariffToRules.setRuleOrdinal(rule.getOrdinal());
            tariffToRules.setTariffRule(IEntity.mapId(rule.getRule().getRuleId(), TariffRule::new));
            return tariffToRules;
        }).toList();
    }

    private List<AppliedTariffRuleDto> rulesToDto(List<TariffToRules> entities) {
        return entities.stream().map(entity -> {
            AppliedTariffRuleDto dto = new AppliedTariffRuleDto();
            dto.setRule(tariffRuleMapper.toDto(entity.getTariffRule()));
            dto.setOrdinal(entity.getRuleOrdinal());
            return dto;
        }).toList();
    }
}
