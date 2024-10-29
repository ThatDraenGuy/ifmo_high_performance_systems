package ru.draen.hps.common.dbms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.draen.hps.common.jpadao.entity.ADeletableEntity;

import java.time.Instant;

@Entity
@Table(name = "cdr_files")
@Getter
@Setter
public class CdrFile extends ADeletableEntity<Long> {
    @Id
    @Column(name = "cdrf_file_id")
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cdrf_file_id", nullable = false)
    @MapsId
    private File file;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time", nullable = false)
    private Instant endTime;
}
