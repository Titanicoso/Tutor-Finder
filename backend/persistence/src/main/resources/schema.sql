CREATE TABLE IF NOT EXISTS areas (
area_id SERIAL PRIMARY KEY,
name VARCHAR(128) UNIQUE NOT NULL,
image BYTEA NOT NULL,
description VARCHAR(512) NOT NULL
);

CREATE TABLE IF NOT EXISTS subjects (
subject_id SERIAL PRIMARY KEY,
name VARCHAR(128) UNIQUE NOT NULL,
description VARCHAR(512) NOT NULL,
area_id INT NOT NULL,
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
user_id BIGINT NOT NULL,
description VARCHAR(512) NOT NULL,
profile_picture BYTEA NOT NULL,
FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE,
PRIMARY KEY(user_id)
);

CREATE TABLE IF NOT EXISTS courses (
user_id BIGINT NOT NULL,
subject_id BIGINT NOT NULL,
description VARCHAR(512) NOT NULL,
price REAL NOT NULL,
FOREIGN KEY(subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE,
FOREIGN KEY(user_id) REFERENCES professors(user_id) ON DELETE CASCADE,
PRIMARY KEY(user_id, subject_id)
);

CREATE TABLE IF NOT EXISTS conversations (
conversation_id BIGSERIAL PRIMARY KEY,
user_id BIGINT NOT NULL,
professor_id BIGINT NOT NULL,
subject_id BIGINT NOT NULL,
FOREIGN KEY(subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE,
FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE,
FOREIGN KEY(professor_id) REFERENCES professors(user_id) ON DELETE CASCADE,
UNIQUE(user_id, subject_id, professor_id)
);

CREATE TABLE IF NOT EXISTS messages (
message_id BIGSERIAL PRIMARY KEY,
conversation_id BIGINT NOT NULL,
sender_id BIGINT NOT NULL,
message VARCHAR(1024) NOT NULL,
created TIMESTAMP NOT NULL DEFAULT NOW(),
FOREIGN KEY(conversation_id) REFERENCES conversations(conversation_id) ON DELETE CASCADE,
FOREIGN KEY(sender_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS schedules (
user_id BIGINT NOT NULL,
day INTEGER NOT NULL,
hour INTEGER NOT NULL,
FOREIGN KEY(user_id) REFERENCES professors(user_id) ON DELETE CASCADE,
PRIMARY KEY(user_id, day, hour)
);

CREATE TABLE IF NOT EXISTS reset_password_tokens (
id BIGSERIAL PRIMARY KEY,
user_id BIGINT NOT NULL,
token CHAR(36) NOT NULL,
expires TIMESTAMP NOT NULL,
FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS course_requests (
id BIGSERIAL PRIMARY KEY,
course_user_id BIGINT NOT NULL,
course_subject_id BIGINT NOT NULL,
student_user_id BIGINT NOT NULL,
day INT NOT NULL,
starttime TIMESTAMP NOT NULL,
endtime TIMESTAMP NOT NULL,
status INT NOT NULL,
comment VARCHAR(255),
FOREIGN KEY(student_user_id) REFERENCES users(user_id) ON DELETE CASCADE,
FOREIGN KEY(course_user_id, course_subject_id) courses(user_id, subject_id) ON DELETE CASCADE
);