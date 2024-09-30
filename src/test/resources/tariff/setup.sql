INSERT INTO tariffs (oper_oper_id, name) VALUES (3, 'Тестовый тариф');

INSERT INTO tariff_hist (trff_trff_id, start_date) VALUES (1, to_date('01012010', 'ddmmyyyy'));

INSERT INTO tariff_to_rules (trffh_trffh_id, trfrl_trfrl_id, rule_ordinal) VALUES
(1, 1, 0), -- стандартное правило
(1, 2, 1); -- завершающее правило