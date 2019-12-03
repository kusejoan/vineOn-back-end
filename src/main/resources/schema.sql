
CREATE TABLE IF NOT EXISTS USERS (
  id                SERIAL PRIMARY KEY,
  first_name        VARCHAR(100),
  last_name         VARCHAR(100),
  user_name         VARCHAR(100),
  password          VARCHAR(255) -- Hashed by SHA256

  );

CREATE TABLE IF NOT EXISTS ROLES
(
  id                SERIAL PRIMARY KEY,
  name              VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS USERS_ROLES
(
  users_id INT,
  roles_id INT
);