
CREATE TABLE IF NOT EXISTS USERS (
  id                SERIAL PRIMARY KEY,
  user_name         VARCHAR(100) NOT NULL,
  password          VARCHAR(255) CHECK  (length(password) >= 8),
  role_id           INT NOT NULL

  );

CREATE TABLE IF NOT EXISTS ROLES(
  id                SERIAL PRIMARY KEY,
  name              VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS USER_INFO(
  id                INT NOT NULL REFERENCES USERS(id),
  user_name         VARCHAR(100) NOT NULL,
  informations      VARCHAR(255) NOT NULL

  );

CREATE TABLE IF NOT EXISTS STORE(
  id                INT NOT NULL REFERENCES USERS(id),
  id_store          SERIAL PRIMARY KEY,
  informations      VARCHAR(255) NOT NULL

);


CREATE TABLE IF NOT EXISTS STORE_GRADES(
  id_grade          SERIAL PRIMARY KEY,
  id_store          INT NOT NULL REFERENCES STORE(id_store),
  id_user           INT NOT NULL REFERENCES USERS(id)

);


CREATE TABLE IF NOT EXISTS STORE_WINE(
  id_store          INT NOT NULL REFERENCES STORE(id_store),
  id_wine           INT NOT NULL REFERENECES WINE(id_wine)

);


CREATE TABLE IF NOT EXISTS USER_WINE(
  id                VARCHAR(100),
  id_wine           VARCHAR(100)

);

CREATE TABLE IF NOT EXISTS WINE_GRADES(
  id                SERIAL PRIMARY KEY,
  id_wine           INT NOT NULL REFERENCES WINE(id_wine),
  id_user           VARCHAR(100)

);

CREATE TABLE IF NOT EXISTS WINE(
  id_wine           SERIAL PRIMARY KEY,
  informations      VARCHAR(100)

);
