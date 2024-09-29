CREATE TABLE cdr_files(
    cdrf_file_id INT8 PRIMARY KEY REFERENCES files(file_id),
    start_time TIMESTAMP WITH TIME ZONE NULL,
    end_time TIMESTAMP WITH TIME ZONE NULL,
    del_date TIMESTAMP WITH TIME ZONE NULL,
    del_user VARCHAR NULL
);

CREATE TABLE cdr_data(
    cdrd_id SERIAL8 PRIMARY KEY,
    cdrf_file_id INT8 NOT NULL REFERENCES cdr_files(cdrf_file_id),
    cli_cli_id INT8 NOT NULL REFERENCES clients(cli_id),
    direction CHAR(3) NOT NULL,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE NOT NULL,
    duration INT8 NULL,
    cost DECIMAL(24, 10) NULL,
    cur_cur_id INT8 REFERENCES currencies(cur_id),
    del_date TIMESTAMP WITH TIME ZONE NULL,
    del_user VARCHAR NULL
    CONSTRAINT cdr_data_direction_chk CHECK (direction::text = ANY (ARRAY['INC', 'OUT']::text[]))
);