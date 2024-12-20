package ru.draen.hps.common.dbms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.draen.hps.common.jpadao.entity.ADeletableEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "tariff_rules")
@Getter
@Setter
public class TariffRule extends ADeletableEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tariff_rules_trfrl_id_gen")
    @SequenceGenerator(name = "tariff_rules_trfrl_id_gen", sequenceName = "tariff_rules_trfrl_id_seq", allocationSize = 1)
    @Column(name = "trfrl_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "oper_oper_id")
    private Operator operator;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "minute_cost", nullable = false)
    private BigDecimal minuteCost;

    @Column(name = "minute_limit")
    private Integer minuteLimit;
}
