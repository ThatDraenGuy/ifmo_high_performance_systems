INSERT INTO tariffs (oper_oper_id, name, del_date, del_user) VALUES
(3, 'Тестовый тариф', NULL, NULL),
(3, 'Длинный тариф', NULL, NULL),
(3, 'Удалённый тариф', to_date('01012011', 'ddmmyyyy'), 'Anonymous user');

INSERT INTO tariff_hist (trff_trff_id, start_date, end_date, del_date, del_user) VALUES
(1, to_date('01012010', 'ddmmyyyy'), to_date('31122999', 'ddmmyyyy'), NULL, NULL),
(2, to_date('01012009', 'ddmmyyyy'), to_date('01062009', 'ddmmyyyy'), NULL, NULL),
(2, to_date('01062009', 'ddmmyyyy'), to_date('31122999', 'ddmmyyyy'), NULL, NULL),
(3, to_date('01062010', 'ddmmyyyy'), to_date('31122999', 'ddmmyyyy'), to_date('01012011', 'ddmmyyyy'), 'Anonymous user');

INSERT INTO tariff_to_rules (trffh_trffh_id, trfrl_trfrl_id, rule_ordinal) VALUES
(1, 1, 0),
(1, 2, 1),
(2, 1, 0),
(2, 3, 1),
(2, 2, 2),
(3, 1, 0),
(3, 4, 1),
(3, 2, 2),
(4, 2, 0);