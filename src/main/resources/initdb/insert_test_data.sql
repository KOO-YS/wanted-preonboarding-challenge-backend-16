INSERT INTO `performance` (name, price, round, type, start_date, is_reserve)
VALUES ('레베카', 100000, 1, 0, '2024-01-20 19:30:00', 'disable');

INSERT INTO performance_seat_info VALUES
 (DEFAULT, (SELECT id FROM performance limit 1), 1, 1, 'A', 1, 'enable', DEFAULT, DEFAULT)
,(DEFAULT, (SELECT id FROM performance limit 1), 1, 1, 'A', 2, 'enable', DEFAULT, DEFAULT)
,(DEFAULT, (SELECT id FROM performance limit 1), 1, 1, 'A', 3, 'enable', DEFAULT, DEFAULT)
,(DEFAULT, (SELECT id FROM performance limit 1), 1, 1, 'A', 4, 'enable', DEFAULT, DEFAULT);

INSERT INTO ticket_cancel_notification
VALUES (DEFAULT, (SELECT id FROM performance limit 1), "유진호", "010-1234-1234", "jhy7342@gmail.com", DEFAULT,
        DEFAULT, DEFAULT, DEFAULT);


#INSERT INTO performance_discount_policy (performance_id, type, name, rate, discount_fee)
#VALUES (uuid_to_bin("3e748812-9ab5-11ef-b010-0242ac120002"), "할인률", "telecom", 5, 0);

INSERT INTO performance_discount_policy (performance_id, type, name, rate, discount_fee)
    SELECT
        performance.id, "할인률", "telecome", 10, 0
    FROM
        performance
WHERE
    performance.id = (SELECT MAX(id) FROM performance);


INSERT INTO performance_discount_policy (performance_id, type, name, rate, discount_fee)
    SELECT
        performance.id, "일정금액", "new_member", 0, 10000
    FROM
        performance
WHERE
    performance.id = (SELECT MAX(id) FROM performance);

INSERT INTO performance_discount_policy (performance_id, type, name, rate, discount_fee)
SELECT
    performance.id, "할인률", "okcashback", 5, 0
FROM
    performance
WHERE
    performance.id = (SELECT MAX(id) FROM performance);

INSERT INTO performance_discount_policy (performance_id, type, name, rate, discount_fee)
SELECT
    performance.id, "할인률", "happy_point", 5, 0
FROM
    performance
WHERE
    performance.id = (SELECT MAX(id) FROM performance);