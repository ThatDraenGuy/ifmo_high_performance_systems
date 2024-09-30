package ru.draen.hps.app.tariff.dao;

import ru.draen.hps.common.dao.IHistoricalRepository;
import ru.draen.hps.common.dao.ISearchRepository;
import ru.draen.hps.domain.TariffHist;

public interface TariffHistRepository extends ISearchRepository<TariffHist, Long>, IHistoricalRepository<TariffHist, Long> {
}
