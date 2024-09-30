package ru.draen.hps.app.tariff.service;

import ru.draen.hps.app.tariff.controller.dto.TariffCondition;
import ru.draen.hps.common.service.ICrudService;
import ru.draen.hps.common.service.ISearchService;
import ru.draen.hps.domain.TariffHist;

public interface TariffService extends ICrudService<TariffHist, Long>, ISearchService<TariffHist, TariffCondition> {
}
