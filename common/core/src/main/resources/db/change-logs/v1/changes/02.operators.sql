CREATE TABLE operators (
    oper_id SERIAL8 PRIMARY KEY,
    name VARCHAR(100),
    code CHAR(5),
    del_date TIMESTAMP WITH TIME ZONE NULL,
    del_user VARCHAR NULL,
    CONSTRAINT operator_name_chk EXCLUDE (name WITH =) WHERE (del_date IS NULL),
    CONSTRAINT operator_code_chk EXCLUDE (code WITH =) WHERE (del_date IS NULL)
);

CREATE TABLE languages (
    lang_id INT8 PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code CHAR(3) UNIQUE NOT NULL
);

INSERT INTO languages (lang_id, name, code) VALUES
(1, 'Русский', 'RUS'),
(2, 'Английский', 'ENG');

CREATE TABLE operator_languages (
    oper_oper_id INT8 REFERENCES operators(oper_id),
    lang_lang_id INT8 REFERENCES languages(lang_id),
    CONSTRAINT operator_languages_pk PRIMARY KEY (oper_oper_id, lang_lang_id)
);