package ru.draen.hps.common.dbms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.draen.hps.common.jpadao.entity.ADeletableEntity;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "operators")
@Getter
@Setter
public class Operator extends ADeletableEntity<Long> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "files_file_id_gen")
    @SequenceGenerator(name = "files_file_id_gen", sequenceName = "files_file_id_seq", allocationSize = 1)
    @Column(name = "oper_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "operator_languages",
            joinColumns =           @JoinColumn(name = "oper_oper_id", referencedColumnName = "oper_id"),
            inverseJoinColumns =    @JoinColumn(name = "lang_lang_id", referencedColumnName = "lang_id"))
    private List<Language> languages;
}
