CREATE TABLE files (
    file_id SERIAL8 PRIMARY KEY,
    file_name VARCHAR NOT NULL,
    oper_oper_id INT8 REFERENCES operators(oper_id) NOT NULL,
    del_date TIMESTAMP WITH TIME ZONE NULL,
    del_user VARCHAR NULL
);

CREATE TABLE file_content (
    file_file_id INT8 PRIMARY KEY REFERENCES files(file_id),
    check_sum INT8 NOT NULL,
    data_id INT8 NOT NULL
);
