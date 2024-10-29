package ru.draen.hps.common.dbms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.draen.hps.common.jpadao.entity.IEntity;

@Entity
@Table(name = "languages")
@Getter
@Setter
public class Language implements IEntity<Long> {
    @Id
    @Column(name = "lang_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;
}