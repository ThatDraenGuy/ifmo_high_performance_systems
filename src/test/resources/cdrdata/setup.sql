INSERT INTO cdr_data (cdrd_id, cdrf_file_id, cli_cli_id, direction, start_time, end_time) VALUES
(101, 101, 101, 'INC', '2024-09-17T04:39:00Z', '2024-09-17T04:44:15Z'),
(102, 101, 101, 'INC', '2024-09-17T05:39:00Z', '2024-09-17T08:44:15Z'),
(103, 101, 101, 'INC', '2024-09-17T09:39:00Z', '2024-09-17T10:44:15Z'),
(104, 101, 101, 'INC', '2024-09-17T11:39:00Z', '2024-09-17T12:44:15Z'),
(110, 110, 103, 'INC', '2024-09-17T04:39:00Z', '2024-09-18T04:44:15Z');

INSERT INTO cdr_data (cdrd_id, cdrf_file_id, rprt_rprt_id, cli_cli_id, direction, start_time, end_time, minutes, cost) VALUES
(105, 101, 101, 101, 'OUT', '2024-09-17T04:35:00Z', '2024-09-17T04:37:15Z', 3, 5.0);