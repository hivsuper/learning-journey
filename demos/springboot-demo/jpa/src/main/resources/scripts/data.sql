INSERT INTO customer(id, name,email,created_date) VALUES
(1,'111','111@yahoo.com', '2017-02-11'),
(2,'222','222@yahoo.com', '2017-02-12'),
(3,'333','333@yahoo.com', '2017-02-13');

INSERT INTO customer_password (customer_id, password, created_date, modified_date) VALUES
(1, '11111a', '2024-06-28', '2024-06-30'),
(2, '22222b', '2024-06-29', '2024-06-30'),
(3, '33333c', '2024-06-30', '2024-06-30');

INSERT INTO customer_ops_log (customer_id, event, created_date) VALUES
(1, 'create password', '2024-06-28'),
(1, 'create password', '2024-06-29'),
(2, 'create password', '2024-06-30');