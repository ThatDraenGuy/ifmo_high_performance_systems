package ru.draen.hps.common.dbms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.draen.hps.common.jpadao.entity.ADeletableEntity;

import java.io.Serializable;

@Entity
@Table(name = "clients")
@Getter
@Setter
public class Client extends ADeletableEntity<Long> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clients_cli_id_gen")
    @SequenceGenerator(name = "clients_cli_id_gen", sequenceName = "clients_cli_id_seq", allocationSize = 1)
    @Column(name = "cli_id")
    private Long id;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "oper_oper_id")
    private Operator operator;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trff_trff_id")
    private Tariff tariff;
}
