package ru.draen.hps.app.cdrdata.controller.mapper;

import ru.draen.hps.app.cdrdata.controller.dto.CdrDataDto;
import ru.draen.hps.common.annotation.Mapper;
import ru.draen.hps.common.exception.NotImplementedException;
import ru.draen.hps.common.mapper.IMapper;
import ru.draen.hps.domain.CdrData;

@Mapper
public class CdrDataMapper implements IMapper<CdrData, CdrDataDto> {
    @Override
    public CdrData toEntity(CdrDataDto dto) {
        throw new NotImplementedException();
    }

    @Override
    public CdrDataDto toDto(CdrData entity) {
        return CdrDataDto.of(entity);
    }

}
