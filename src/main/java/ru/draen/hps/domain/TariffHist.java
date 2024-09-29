package ru.draen.hps.domain;

import jakarta.persistence.*;
import lombok.*;
import ru.draen.hps.common.entity.AHistoricalEntity;



@Entity
@Table(name = "tariff_hist")
@Getter
@Setter
public class TariffHist extends AHistoricalEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tariff_hist_trffh_id_gen")
    @SequenceGenerator(name = "tariff_hist_trffh_id_gen", sequenceName = "tariff_hist_trffh_id_seq", allocationSize = 1)
    @Column(name = "trffh_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trff_trff_id", referencedColumnName = "id")
    private Tariff tariff;

    @ManyToOne
    @JoinColumn(name = "trfrl_trfrl_id", referencedColumnName = "id")
    private TariffRule tariffRule;

    @Column(name = "rule_ordinal")
    private Integer ruleOrdinal;

}
