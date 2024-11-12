package ru.draen.hps.billing.app.tariff.dao;

import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.dbms.domain.TariffHist;
import ru.draen.hps.common.dbms.domain.TariffHist_;
import ru.draen.hps.common.dbms.domain.TariffToRules;
import ru.draen.hps.common.dbms.domain.Tariff_;
import ru.draen.hps.common.jpadao.dao.AHistoricalRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@AllArgsConstructor
public class TariffHistRepositoryImpl extends AHistoricalRepository<TariffHist, Long> implements TariffHistRepository {
    private final TariffToRulesRepository tariffToRulesRepository;

    @Override
    protected @NonNull Class<TariffHist> getEntityClass() {
        return TariffHist.class;
    }

    @Override
    protected void defaultFetchProfile(@NonNull Root<TariffHist> root) {
        root.fetch(TariffHist_.tariff).fetch(Tariff_.operator);
    }

    @Override
    protected @NonNull List<TariffHist> modifyFindResult(@NonNull List<TariffHist> foundEntities) {
        Set<Long> tariffIds = foundEntities.stream().map(TariffHist::getId).collect(Collectors.toUnmodifiableSet());
        Stream<TariffToRules> rules = tariffToRulesRepository.findStream(TariffToRulesSpecification.byTariffs(tariffIds));
        var groupedRules = rules.collect(Collectors.groupingBy(
                rule -> rule.getTariffHist().getId()
        ));
        foundEntities.forEach(entity -> entity.setRules(groupedRules.getOrDefault(entity.getId(), List.of())));
        return foundEntities;
    }

    @Override
    public Specification<TariffHist> logicalKey(@NonNull TariffHist entity) {
        return TariffHistSpecification.logicalKey(entity);
    }
}
