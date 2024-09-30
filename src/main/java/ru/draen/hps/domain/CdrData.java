package ru.draen.hps.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ru.draen.hps.common.entity.ADeletableEntity;

import java.math.BigDecimal;
import java.time.Instant;

import static java.util.Objects.isNull;

@Entity
@Table(name = "cdr_data")
@Getter
@Setter
public class CdrData extends ADeletableEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cdr_data_cdrd_id_gen")
    @SequenceGenerator(name = "cdr_data_cdrd_id_gen", sequenceName = "cdr_data_cdrd_id_seq", allocationSize = 1)
    @Column(name = "cdrd_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cdrf_file_id")
    private CdrFile cdrFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rprt_rprt_id")
    private Report report;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cli_cli_id")
    private Client client;

    @Column(name = "direction", nullable = false)
    @Enumerated(EnumType.STRING)
    private ECallDirection direction;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @Column(name = "minutes")
    private Integer minutes;

    @Column(name = "cost")
    private BigDecimal cost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cur_cur_id")
    private Currency currency;

    public void addToCost(@NonNull BigDecimal other) {
        if (isNull(cost)){
            cost = other;
        } else {
            cost = cost.add(other);
        }
    }
}
