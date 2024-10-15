INSERT INTO tariffs (trff_id, oper_oper_id, name, del_date, del_user) VALUES
(101, 103, 'Тестовый тариф', NULL, NULL),
(102, 103, 'Длинный тариф', NULL, NULL),
(103, 103, 'Удалённый тариф', to_date('01012011', 'ddmmyyyy'), 'Anonymous user'),
(104, 103, 'Невалидный тариф', NUll, NULL);

INSERT INTO tariff_hist (trffh_id, trff_trff_id, start_date, end_date, del_date, del_user) VALUES
(101, 101, to_date('01012010', 'ddmmyyyy'), to_date('31122999', 'ddmmyyyy'), NULL, NULL),
(102, 102, to_date('01012009', 'ddmmyyyy'), to_date('01062009', 'ddmmyyyy'), NULL, NULL),
(103, 102, to_date('01062009', 'ddmmyyyy'), to_date('31122999', 'ddmmyyyy'), NULL, NULL),
(104, 103, to_date('01062010', 'ddmmyyyy'), to_date('31122999', 'ddmmyyyy'), to_date('01012011', 'ddmmyyyy'), 'Anonymous user'),
(105, 104, to_date('01012010', 'ddmmyyyy'), to_date('31122999', 'ddmmyyyy'), NULL, NULL);

INSERT INTO tariff_to_rules (trffh_trffh_id, trfrl_trfrl_id, rule_ordinal) VALUES
(101, 101, 0),
(101, 102, 1),
(102, 101, 0),
(102, 103, 1),
(102, 102, 2),
(103, 101, 0),
(103, 104, 1),
(103, 102, 2),
(104, 102, 0),
(105, 101, 0);