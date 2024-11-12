package ru.draen.hps.common.r2dbcdao.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("files")
@Getter
@Setter
public class File {
    @Id
    @Column("file_id")
    private Long id;

    @Column("file_name")
    private String fileName;

    @Column("oper_oper_id")
    private Long operatorId;
}
