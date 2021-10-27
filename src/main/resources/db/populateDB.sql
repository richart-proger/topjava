DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;
ALTER SEQUENCE global_meal_seq RESTART WITH 1;
DELETE FROM meals;


INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, dateTime, description, calories)
VALUES (100000, '2020-12-19 10:07:04', 'Breakfast', 416),
       (100001, '2021-07-11 06:51:10', 'Breakfast', 2128),
       (100000, '2020-11-12 05:31:33', 'Afternoon snack', 1084),
       (100001, '2021-04-15 07:37:50', 'Afternoon snack', 1907),
       (100000, '2021-03-12 10:28:08', 'Breakfast', 637),
       (100001, '2021-04-27 17:25:54', 'Lunch', 1021);