package ru.draen.hps.common.dbms.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.draen.hps.common.jpadao.entity.ADeletableEntity;

@Entity
@Table(name = "files")
@Getter
@Setter
public class File extends ADeletableEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "files_file_id_gen")
    @SequenceGenerator(name = "files_file_id_gen", sequenceName = "files_file_id_seq", allocationSize = 1)
    @Column(name = "file_id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "oper_oper_id")
    private Operator operator;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id") // делаем Join с этой стороны из-за проблемы с lazy-фетчингом
    private FileContent content;
}
