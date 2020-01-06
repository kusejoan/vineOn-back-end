
CREATE TABLE IF NOT EXISTS USERS (
  id                SERIAL PRIMARY KEY,
  user_name         VARCHAR(100),
  password          VARCHAR(255),
  role_id           INT

  );

CREATE TABLE IF NOT EXISTS ROLES(
  id                SERIAL PRIMARY KEY,
  name              VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS CUSTOMER(
  id                SERIAL PRIMARY KEY,
  first_name        VARCHAR(100),
  surname           VARCHAR(100),
  birth_date         VARCHAR(100)

  );

CREATE TABLE IF NOT EXISTS STORE(
  id                SERIAL PRIMARY KEY,
  store_name        VARCHAR(100) UNIQUE,
  address           VARCHAR(100),
  city              VARCHAR(100),
  country           VARCHAR(100),
  website           VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS WINES(
  id         SERIAL PRIMARY KEY,
  name       VARCHAR(100),
  country    VARCHAR(100),
  color      VARCHAR(100),
  type       VARCHAR(100),
  year       INT,

  description       TEXT
);


CREATE TABLE IF NOT EXISTS STORE_GRADES(
  id                 SERIAL PRIMARY KEY,
  store_id           INT,
  user_id           INT,
  grade              INT,
  description        TEXT
);

CREATE TABLE IF NOT EXISTS WINE_GRADES(
  id                 SERIAL,
  wine_id           INT,
  user_id           INT,
  grade              INT,
  description        TEXT
);


CREATE TABLE IF NOT EXISTS STORE_WINES(
  store_id           INT,
  wines_id           INT
);


CREATE TABLE IF NOT EXISTS USERS_WINES(
  users_id               INT,
  wines_id               INT
);

