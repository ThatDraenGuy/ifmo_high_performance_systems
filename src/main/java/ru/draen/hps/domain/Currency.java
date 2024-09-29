package ru.draen.hps.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import ru.draen.hps.common.entity.IEntity;

@Entity
@Table(name = "currencies")
@Getter
@Setter
public class Currency implements IEntity<Long> {
    @Id
    @Column(name = "cur_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;
}
