
CREATE TABLE IF NOT EXISTS USERS (
  id                SERIAL PRIMARY KEY,
  user_name         VARCHAR(100),
  password          VARCHAR(255),
  role_id           INT

  );

CREATE TABLE IF NOT EXISTS ROLES
(
  id                SERIAL PRIMARY KEY,
  name              VARCHAR(100)
);