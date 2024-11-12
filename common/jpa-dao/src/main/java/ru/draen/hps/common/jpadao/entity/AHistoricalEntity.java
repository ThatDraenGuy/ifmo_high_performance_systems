package ru.draen.hps.common.jpadao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ru.draen.hps.common.core.utils.TimestampHelper;

import java.time.Instant;

import static java.util.Objects.isNull;

@MappedSuperclass
@Getter
@Setter
public abstract class AHistoricalEntity<ID> extends ADeletableEntity<ID> {
    @Column(name = "start_date")
    protected Instant startDate;

    @Column(name = "end_date")
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
