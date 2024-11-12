CREATE TABLE cdr_files(
    cdrf_file_id INT8 PRIMARY KEY,
    start_time TIMESTAMP WITH TIME ZONE NULL,
    end_time TIMESTAMP WITH TIME ZONE NULL,
    del_date TIMESTAMP WITH TIME ZONE NULL,
    del_user VARCHAR NULL
);

CREATE TABLE reports(
    rprt_id SERIAL8 PRIMARY KEY,
    oper_oper_id INT8 NOT NULL,
    cli_cli_id INT8 NOT NULL,
    total_cost DECIMAL(24, 10) NOT NULL,
    total_minutes INT4 NOT NULL,
    start_time TIMESTAMP WITH TIME ZONE NULL,
    end_time TIMESTAMP WITH TIME ZONE NULL,
    del_date TIMESTAMP WITH TIME ZONE NULL,
    del_user VARCHAR NULL
);

CREATE TABLE cdr_data(
    cdrd_id SERIAL8 PRIMARY KEY,
    cdrf_file_id INT8 NOT NULL REFERENCES cdr_files(cdrf_file_id),
    rprt_rprt_id INT8 NULL REFERENCES reports(rprt_id),
    cli_cli_id INT8 NOT NULL,
    direction CHAR(3) NOT NULL,
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE NOT NULL,
    minutes INT4 NULL,
    cost DECIMAL(24, 10) NULL,
    cur_cur_id INT8,
    del_date TIMESTAMP WITH TIME ZONE NULL,
    del_user VARCHAR NULL,
    CONSTRAINT cdr_data_direction_chk CHECK (direction::text = ANY (ARRAY['INC', 'OUT']::text[]))
);
