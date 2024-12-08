package ru.draen.hps.common.dbms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.draen.hps.common.jpadao.entity.ADeletableEntity;

import java.io.Serializable;

@Entity
@Table(name = "tariffs")
@Getter
@Setter
public class Tariff extends ADeletableEntity<Long> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tariffs_trff_id_gen")
    @SequenceGenerator(name = "tariffs_trff_id_gen", sequenceName = "tariffs_trff_id_seq", allocationSize = 1)
    @Column(name = "trff_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "oper_oper_id")
    private Operator operator;

    @Column(name = "name", nullable = false)
    private String name;
}
