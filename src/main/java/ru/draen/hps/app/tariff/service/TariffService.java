package ru.draen.hps.app.tariff.service;

import org.springframework.data.jpa.domain.Specification;
import ru.draen.hps.app.tariff.controller.dto.TariffCondition;
import ru.draen.hps.common.service.ICrudService;
import ru.draen.hps.common.service.ISearchService;
import ru.draen.hps.domain.Tariff;
import ru.draen.hps.domain.TariffHist;

import java.util.Optional;

public interface TariffService extends ICrudService<TariffHist, Long>, ISearchService<TariffHist, TariffCondition> {
    Optional<Tariff> findById(Long id);
    Optional<TariffHist> findOne(Specification<TariffHist> spec);
}
