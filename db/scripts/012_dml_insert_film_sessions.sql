INSERT INTO film_sessions
    (film_id, hall_id, start_time, end_time, price)
VALUES
(9, 1, CURRENT_DATE + TIME '9:00', CURRENT_DATE + TIME '11:00', 300),
(7, 2, CURRENT_DATE + TIME '9:00', CURRENT_DATE + TIME '11:00', 300),
(9, 3, CURRENT_DATE + TIME '9:00', CURRENT_DATE + TIME '11:00', 300),
(10, 1, CURRENT_DATE + TIME '11:20', CURRENT_DATE + TIME '14:40', 600),
(4, 2, CURRENT_DATE + TIME '11:20', CURRENT_DATE + TIME '13:40', 600),
(1, 3, CURRENT_DATE + TIME '11:20', CURRENT_DATE + TIME '13:40', 600),
(5, 1, CURRENT_DATE + TIME '15:00', CURRENT_DATE + TIME '17:50', 700),
(11, 2, CURRENT_DATE + TIME '14:00', CURRENT_DATE + TIME '17:00', 700),
(6, 3, CURRENT_DATE + TIME '14:00', CURRENT_DATE + TIME '17:00', 700),
(8, 1, CURRENT_DATE + TIME '18:10', CURRENT_DATE + TIME '21:00', 900),
(2, 2, CURRENT_DATE + TIME '17:20', CURRENT_DATE + TIME '20:20', 900),
(10, 3, CURRENT_DATE + TIME '17:20', CURRENT_DATE + TIME '20:20', 900),
(5, 1, CURRENT_DATE + TIME '21:20', CURRENT_DATE + TIME '23:50', 900),
(4, 2, CURRENT_DATE + TIME '20:40', CURRENT_DATE + TIME '23:40', 900),
(3, 3, CURRENT_DATE + TIME '20:40', CURRENT_DATE + TIME '23:40', 900);