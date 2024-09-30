package ru.draen.hps.app.tariff.dao;

import lombok.NonNull;
import ru.draen.hps.domain.Tariff;

public interface TariffRepository {
    Tariff save(@NonNull Tariff tariff);
}
