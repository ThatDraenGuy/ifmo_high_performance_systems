package ru.draen.hps.common.r2dbcdao.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.draen.hps.common.r2dbcdao.entity.ADeletableEntity;

import java.time.Instant;

@Table("cdr_files")
@Getter
@Setter
public class CdrFile extends ADeletableEntity<Long> {
    @Id
    @Column("cdrf_file_id")
    private Long id;

//    @Column("oper_oper_id")
//    private Long operatorId;

    @Column("start_time")
    private Instant startTime;

    @Column("end_time")
    private Instant endTime;
}
