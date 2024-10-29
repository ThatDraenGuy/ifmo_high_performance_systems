package ru.draen.hps.common.r2dbcdao.entity;


import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import ru.draen.hps.common.core.utils.TimestampHelper;

import java.time.Instant;

import static java.util.Objects.isNull;

@Getter
@Setter
public abstract class AHistoricalEntity<ID> extends ADeletableEntity<ID> {
    @Column("start_date")
    protected Instant startDate;

    @Column("end_date")
    protected Instant endDate;

    public EHistStatus getHistStatus() {
        return getHistStatus(TimestampHelper.current());
    }

    public EHistStatus getHistStatus(@NonNull Instant moment) {
        if (isDeleted()) return EHistStatus.DELETED;
        if (moment.isBefore(startDate)) return EHistStatus.FUTURE;
        if (isNull(endDate) || moment.isBefore(endDate)) return EHistStatus.ACTIVE;
        return EHistStatus.OBSOLETE;
    }
}
