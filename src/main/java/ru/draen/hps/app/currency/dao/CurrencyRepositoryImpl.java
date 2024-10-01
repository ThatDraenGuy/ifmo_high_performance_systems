package ru.draen.hps.app.currency.dao;

import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import org.springframework.stereotype.Repository;
import ru.draen.hps.common.dao.AGenericRepository;
import ru.draen.hps.domain.Currency;

@Repository
public class CurrencyRepositoryImpl extends AGenericRepository<Currency, Long> implements CurrencyRepository {
    @Override
    protected @NonNull Class<Currency> getEntityClass() {
        return Currency.class;
    }

    @Override
    protected void defaultFetchProfile(@NonNull Root<Currency> root) {
    }
}
