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
public class CdrFileCreate extends ADeletableEntity<Long> {
    @Column("cdrf_file_id")
    private Long id;

    @Column("start_time")
    private Instant startTime;

    @Column("end_time")
    private Instant endTime;

    public CdrFile toEntity() {
        CdrFile entity = new CdrFile();
        entity.setId(id);
        entity.setStartTime(startTime);
        entity.setEndTime(endTime);
        entity.setDelDate(delDate);
        entity.setDelUser(delUser);
        return entity;
    }
}
