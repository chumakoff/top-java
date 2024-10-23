DELETE
FROM meals;
DELETE
FROM user_role;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;
ALTER SEQUENCE meals_id_seq RESTART WITH 1;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, description, date_time, calories)
VALUES (100000, 'User Meal', '2024-10-22 20:01', '101'),
       (100001, 'Admin Meal', '2024-10-22 20:02', '102'),
       (100002, 'Guest Meal 1', '2024-10-22 20:00', '103'),
       (100002, 'Guest Meal 2', '2024-10-23 00:00', '104'),
       (100002, 'Guest Meal 3', '2024-10-24 20:00', '105');
