package ru.draen.hps.app.cdrdata.service;

import ru.draen.hps.app.cdrdata.controller.dto.CdrDataCondition;
import ru.draen.hps.common.service.ISearchService;
import ru.draen.hps.domain.CdrData;

public interface CdrDataService extends ISearchService<CdrData, CdrDataCondition> {
}
