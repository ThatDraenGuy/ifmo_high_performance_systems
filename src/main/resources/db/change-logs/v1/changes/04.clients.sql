CREATE TABLE clients (
    cli_id SERIAL8 PRIMARY KEY,
    phone_number CHAR(11) NOT NULL,
    oper_oper_id INT8 NOT NULL REFERENCES operators(oper_id),
    del_date TIMESTAMP WITH TIME ZONE NULL,
    del_user VARCHAR NULL,
    CONSTRAINT client_phone_number_chk EXCLUDE (phone_number WITH =) WHERE (del_date IS NULL),
    CONSTRAINT client_phone_number_chk2 CHECK (phone_number ~* '^[1-9][0-9]{10}$')
);
