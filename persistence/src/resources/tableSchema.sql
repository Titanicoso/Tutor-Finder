CREATE TABLE IF NOT EXISTS areas (
area_id SERIAL PRIMARY KEY,
name VARCHAR(128) UNIQUE NOT NULL,
description VARCHAR(512) NOT NULL
);

CREATE TABLE IF NOT EXISTS subjects (
subject_id SERIAL PRIMARY KEY,
name VARCHAR(128) UNIQUE NOT NULL,
description VARCHAR(512) NOT NULL,
area_id INT,
FOREIGN KEY(area_id) REFERENCES areas(area_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users (
user_id BIGSERIAL PRIMARY KEY,
username VARCHAR(128) UNIQUE NOT NULL,
password VARCHAR(64) NOT NULL,
email VARCHAR(512) UNIQUE NOT NULL,
name VARCHAR(128) NOT NULL,
lastname VARCHAR(128) NOT NULL
);

CREATE TABLE IF NOT EXISTS professors (
user_id BIGINT,
description VARCHAR(512) NOT NULL,
FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE,
PRIMARY KEY(user_id)
);

CREATE TABLE IF NOT EXISTS courses (
user_id BIGINT,
subject_id BIGINT,
description VARCHAR(512) NOT NULL,
price REAL NOT NULL CHECK(price >= 0),
rating REAL NOT NULL CHECK(rating >= 0 AND rating <= 5),
FOREIGN KEY(user_id) REFERENCES subjects(subject_id) ON DELETE CASCADE,
FOREIGN KEY(subject_id) REFERENCES professors(user_id) ON DELETE CASCADE,
PRIMARY KEY(user_id, subject_id)
);