package ru.draen.hps.domain;

import jakarta.persistence.*;
import lombok.*;
import ru.draen.hps.common.entity.IEntity;

import static java.util.Objects.isNull;


@Entity
@Table(name = "tariff_to_rules")
@IdClass(TariffToRules.PK.class)
@Getter
@Setter
public class TariffToRules implements IEntity<TariffToRules.PK> {
    @Id
    @ManyToOne
    @JoinColumn(name = "trffh_trffh_id", referencedColumnName = "trffh_id")
    private TariffHist tariffHist;

    @Id
    @ManyToOne
    @JoinColumn(name = "trfrl_trfrl_id", referencedColumnName = "trfrl_id")
    private TariffRule tariffRule;

    @Column(name = "rule_ordinal")
    private Integer ruleOrdinal;

    @Override
    public PK getId() {
        return new PK(tariffHist.getId(), tariffRule.getId());
    }

    @Override
    public void setId(PK pk) {
        if (isNull(pk)) {
            this.tariffHist = null;
            this.tariffRule = null;
        } else {
            this.tariffHist = IEntity.mapId(pk.tariffHist, TariffHist::new);
            this.tariffRule = IEntity.mapId(pk.tariffRule, TariffRule::new);
        }
    }

    public record PK(
            Long tariffHist,
            Long tariffRule
    ) {
    }
}
