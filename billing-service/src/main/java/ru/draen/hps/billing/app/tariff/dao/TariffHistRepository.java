package ru.draen.hps.billing.app.tariff.dao;


import ru.draen.hps.common.dbms.domain.TariffHist;
import ru.draen.hps.common.jpadao.dao.IHistoricalRepository;
import ru.draen.hps.common.jpadao.dao.ISearchRepository;

public interface TariffHistRepository extends ISearchRepository<TariffHist, Long>, IHistoricalRepository<TariffHist, Long> {
}
