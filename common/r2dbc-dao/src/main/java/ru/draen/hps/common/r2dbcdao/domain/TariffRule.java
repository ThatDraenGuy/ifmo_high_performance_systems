package ru.draen.hps.common.r2dbcdao.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.draen.hps.common.r2dbcdao.entity.ADeletableEntity;

import java.math.BigDecimal;


@Table("tariff_rules")
@Getter
@Setter
public class TariffRule extends ADeletableEntity<Long> {
    @Id
    @Column("trfrl_id")
    private Long id;

    @Column("name")
    private String name;

    @Column("minute_cost")
    private BigDecimal minuteCost;

    @Column("minute_limit")
    private Integer minuteLimit;
}
