CREATE TABLE currencies(
    cur_id SERIAL8 PRIMARY KEY,
    name VARCHAR NOT NULL,
    code CHAR(3) NOT NULL
);

-- fill table with curs