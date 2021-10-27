DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;
DROP TABLE IF EXISTS meals;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users
(
  id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  name             VARCHAR                           NOT NULL,
  email            VARCHAR                           NOT NULL,
  password         VARCHAR                           NOT NULL,
  registered       TIMESTAMP           DEFAULT now() NOT NULL,
  enabled          BOOL                DEFAULT TRUE  NOT NULL,
  calories_per_day INTEGER             DEFAULT 2000  NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
  user_id INTEGER NOT NULL,
  role    VARCHAR,
  CONSTRAINT user_roles_idx UNIQUE (user_id, role),
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE SEQUENCE global_meal_seq START WITH 100000;

CREATE TABLE meals
(
  id          INTEGER PRIMARY KEY DEFAULT nextval('global_meal_seq'),
  user_id     INTEGER                           NOT NULL,
  dateTime    TIMESTAMP           DEFAULT now() NOT NULL,
  description VARCHAR                           NOT NULL,
  calories    INTEGER             DEFAULT 0     NOT NULL
);

CREATE UNIQUE INDEX meals_unique_user_id_idx ON meals(user_id);
CREATE UNIQUE INDEX meals_unique_date_idx ON meals (dateTime);

INSERT INTO meals (user_id, dateTime, description, calories)
VALUES (100001, '2020-12-19 10:07:04', 'Breakfast', 416),
       (100001, '2021-07-11 06:51:10', 'Breakfast', 2128),
       (100000, '2020-11-12 05:31:33', 'Afternoon snack', 1084),
       (100000, '2021-04-15 07:37:50', 'Afternoon snack', 1907),
       (100000, '2021-03-12 10:28:08', 'Breakfast', 637),
       (100001, '2021-04-27 17:25:54', 'Lunch', 1021);