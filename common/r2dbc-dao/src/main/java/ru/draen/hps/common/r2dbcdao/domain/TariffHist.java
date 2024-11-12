package ru.draen.hps.common.r2dbcdao.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.draen.hps.common.r2dbcdao.entity.AHistoricalEntity;


@Table("tariff_hist")
@Getter
@Setter
public class TariffHist extends AHistoricalEntity<Long> {
    @Id
    @Column("trffh_id")
    private Long id;

    @Column("trff_trff_id")
    private Long tariffId;
}
