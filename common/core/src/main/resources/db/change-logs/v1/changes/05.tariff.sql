CREATE TABLE tariffs(
    trff_id SERIAL8 PRIMARY KEY,
    oper_oper_id INT8 NOT NULL REFERENCES operators(oper_id),
    name VARCHAR NOT NULL,
    del_date TIMESTAMP WITH TIME ZONE NULL,
    del_user VARCHAR NULL
);

CREATE TABLE tariff_hist(
    trffh_id SERIAL8 PRIMARY KEY,
    trff_trff_id INT8 NOT NULL REFERENCES tariffs(trff_id),
    del_date TIMESTAMP WITH TIME ZONE NULL,
    del_user VARCHAR NULL,
    start_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT to_date('31122999', 'ddmmyyyy')
);

CREATE TABLE tariff_rules(
    trfrl_id SERIAL8 PRIMARY KEY,
    oper_oper_id INT8 NOT NULL REFERENCES operators(oper_id),
    name VARCHAR NOT NULL,
    minute_cost DECIMAL(24, 10) NOT NULL,
    minute_limit INT4 NULL,
    del_date TIMESTAMP WITH TIME ZONE NULL,
    del_user VARCHAR NULL
);

CREATE TABLE tariff_to_rules(
    trffh_trffh_id INT8 REFERENCES tariff_hist(trffh_id),
    trfrl_trfrl_id INT8 REFERENCES tariff_rules(trfrl_id),
    rule_ordinal INT4 NOT NULL,
    CONSTRAINT tariff_to_rules_pk PRIMARY KEY (trffh_trffh_id, trfrl_trfrl_id)
);
