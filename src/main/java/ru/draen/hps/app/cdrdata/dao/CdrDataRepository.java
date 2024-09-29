package ru.draen.hps.app.cdrdata.dao;

import lombok.NonNull;
import ru.draen.hps.common.dao.ISearchRepository;
import ru.draen.hps.domain.CdrData;

public interface CdrDataRepository extends ISearchRepository<CdrData, Long> {
    CdrData save(@NonNull CdrData cdrData);
}
