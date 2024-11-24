INSERT INTO tariff_rules (trfrl_id, oper_oper_id, name, minute_cost, minute_limit)
VALUES
(101, 103, 'Стандартное правило', 1.5, 200),
(102, 103, 'Завершающее правило', 3.0, null),
(103, 103, 'Дорогое правило', 5.0, 150),
(104, 103, 'Дешёвое правило', 1.0, 250),
(105, 103, 'Долгое правило', 1.5, 600);