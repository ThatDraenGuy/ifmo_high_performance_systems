CREATE TABLE operators (
    oper_id SERIAL8 PRIMARY KEY,
    name VARCHAR(100),
    code CHAR(5),
    del_date TIMESTAMP WITH TIME ZONE NULL,
    del_user VARCHAR NULL,
    CONSTRAINT operator_name_chk EXCLUDE (name WITH =) WHERE (del_date IS NULL),
    CONSTRAINT operator_code_chk EXCLUDE (code WITH =) WHERE (del_date IS NULL)
);

--INSERT INTO operators (name, code) VALUES
--('SeaNet', 'AAM21'),
--('Aquafon', 'ABKAF'),
--('Мегафон', 'RUSNW');