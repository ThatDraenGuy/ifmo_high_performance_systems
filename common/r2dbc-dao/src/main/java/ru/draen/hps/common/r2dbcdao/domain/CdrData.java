package ru.draen.hps.common.r2dbcdao.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.draen.hps.common.r2dbcdao.entity.ADeletableEntity;

import java.math.BigDecimal;
import java.time.Instant;

import static java.util.Objects.isNull;


@Table("cdr_data")
@Getter
@Setter
public class CdrData extends ADeletableEntity<Long> {
    @Id
    @Column("cdrd_id")
    private Long id;

    @Column("cdrf_file_id")
    private Long cdrFileId;

    @Column("rprt_rprt_id")
    private Long reportId;

    @Column("cli_cli_id")
    private Long clientId;

    @Column("direction")
    private ECallDirection direction;

    @Column("start_time")
    private Instant startTime;

    @Column("end_time")
    private Instant endTime;

    @Column("minutes")
    private Integer minutes;

    @Column("cost")
    private BigDecimal cost;

    public void addToCost(@NonNull BigDecimal other) {
        if (isNull(cost)){
            cost = other;
        } else {
            cost = cost.add(other);
        }
    }
}
