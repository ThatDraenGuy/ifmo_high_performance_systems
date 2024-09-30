package ru.draen.hps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.draen.hps.common.entity.AHistoricalEntity;

import java.util.Comparator;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trff_trff_id")
    private Tariff tariff;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tariffHist")
    private List<TariffToRules> rules;

    public List<TariffRule> getOrderedRules() {
        return rules.stream()
                .sorted(Comparator.comparingInt(TariffToRules::getRuleOrdinal))
                .map(TariffToRules::getTariffRule).toList();
    }
}
