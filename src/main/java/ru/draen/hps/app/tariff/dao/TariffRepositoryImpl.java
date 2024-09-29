package ru.draen.hps.app.tariff.dao;

import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.dao.ADeletableRepository;
import ru.draen.hps.domain.Tariff;
import ru.draen.hps.domain.TariffHist;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@AllArgsConstructor
public class TariffRepositoryImpl extends ADeletableRepository<Tariff, Long> implements TariffRepository {
    private final TariffHistRepository tariffHistRepository;

    @Override
    protected @NonNull Class<Tariff> getEntityClass() {
        return Tariff.class;
    }

    @Override
    protected void defaultFetchProfile(@NonNull Root<Tariff> root) {
    }

    @Override
    protected @NonNull List<Tariff> modifyFindResult(@NonNull List<Tariff> foundEntities) {
        Set<Long> tariffIds = foundEntities.stream().map(Tariff::getId).collect(Collectors.toUnmodifiableSet());
        Stream<TariffHist> history = tariffHistRepository.findStream(TariffHistSpecification.byTariffs(tariffIds));
        var historyByPeriod = history.collect(Collectors.groupingBy(
                hist -> hist.getTariff().getId(),
                Collectors.groupingBy(hist -> Pair.of(hist.getStartDate(), hist.getEndDate()))
        ));
        foundEntities.forEach(entity -> entity.setRules(
                historyByPeriod.getOrDefault(entity.getId(), Map.of())
                        .getOrDefault(Pair.of(), List.of())
        ));
        return foundEntities;
    }
}
