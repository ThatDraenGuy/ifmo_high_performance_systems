package ru.draen.hps.cdr.app.cdrdata.controller.mapper;

import ru.draen.hps.cdr.app.cdrdata.controller.dto.CdrDataDto;
import ru.draen.hps.common.core.annotation.Mapper;
import ru.draen.hps.common.core.exception.NotImplementedException;
import ru.draen.hps.common.core.mapper.IMapper;
import ru.draen.hps.common.core.utils.TimestampHelper;
import ru.draen.hps.common.r2dbcdao.domain.CdrData;

@Mapper
public class CdrDataMapper implements IMapper<CdrData, CdrDataDto> {
    @Override
    public CdrData toEntity(CdrDataDto dto) {
        CdrData entity = new CdrData();
        entity.setId(dto.getCdrDataId());
        entity.setCdrFileId(dto.getCdrFileId());
        entity.setClientId(dto.getClientId());
        entity.setReportId(dto.getReportId());
        entity.setDirection(dto.getDirection());
        entity.setStartTime(TimestampHelper.toInstant(dto.getStartTime()));
        entity.setEndTime(TimestampHelper.toInstant(dto.getEndTime()));
        entity.setMinutes(dto.getMinutes());
        entity.setCost(dto.getCost());
        return entity;
    }

    @Override
    public CdrDataDto toDto(CdrData entity) {
        return CdrDataDto.of(entity);
    }
}
