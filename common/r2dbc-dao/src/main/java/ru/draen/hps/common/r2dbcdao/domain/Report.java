package ru.draen.hps.common.r2dbcdao.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.draen.hps.common.r2dbcdao.entity.ADeletableEntity;

import java.math.BigDecimal;
import java.time.Instant;

import static java.util.Objects.isNull;

@Getter
@Setter
@Table("reports")
public class Report extends ADeletableEntity<Long> {
    @Id
    @Column("rprt_id")
    private Long id;

    @Column("oper_oper_id")
    private Long operatorId;

    @Column("cli_cli_id")
    private Long clientId;

    @Column("total_cost")
    private BigDecimal totalCost;

    @Column("total_minutes")
    private Integer totalMinutes;

    @Column("start_time")
    private Instant startTime;

    @Column("end_time")
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
