package ru.draen.hps.common.r2dbcdao.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.draen.hps.common.r2dbcdao.entity.IEntity;

@Table(name = "file_content")
@Getter
@Setter
public class FileContent implements IEntity<Long> {
    @Id
    @Column("file_file_id")
    private Long id;

    @Column("file_name")
    private String fileName;

    @Column("oper_oper_id")
    private Long operatorId;

    @Column("check_sum")
    private Long checkSum;

    @Column("data_id")
    private Long dataId;

    private byte[] data;
}
