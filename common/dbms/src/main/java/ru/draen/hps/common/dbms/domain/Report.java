package ru.draen.hps.common.dbms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.draen.hps.common.jpadao.entity.ADeletableEntity;

import java.math.BigDecimal;
import java.time.Instant;

import static java.util.Objects.isNull;

@Entity
@Table(name = "reports")
@Getter
@Setter
public class Report extends ADeletableEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reports_rprt_id_gen")
    @SequenceGenerator(name = "reports_rprt_id_gen", sequenceName = "reports_rprt_id_seq", allocationSize = 1)
    @Column(name = "rprt_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "oper_oper_id")
    private Operator operator;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cli_cli_id")
    private Client client;

    @Column(name = "total_cost", nullable = false)
    private BigDecimal totalCost;

    @Column(name = "total_minutes", nullable = false)
    private Integer totalMinutes;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    public void addTotalCost(BigDecimal other) {
        if (isNull(totalCost)) {
            totalCost = other;
        } else {
            totalCost = totalCost.add(other);
        }
    }

    public void addTotalMinutes(int minutes) {
        totalMinutes = totalMinutes + minutes;
    }
}
