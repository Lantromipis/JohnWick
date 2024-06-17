CREATE TABLE application_user
(
    id BIGSERIAL PRIMARY KEY,
    username TEXT UNIQUE NOT NULL,
    display_name TEXT NOT NULL,
    password TEXT NOT NULL,
    role TEXT NOT NULL
);

CREATE TABLE application_order
(
    id BIGSERIAL PRIMARY KEY,
    type TEXT NOT NULL,
    assignee_id BIGINT REFERENCES application_user(id),
    debtor_id BIGINT REFERENCES application_user(id),
    customer TEXT NOT NULL,
    target TEXT,
    price BIGINT NOT NULL,
    description TEXT
);

INSERT INTO application_user (username, display_name, password, role) VALUES
    ('admin', 'admin adminovich', '$2a$10$/FarO5LVt.6SAUGBlYf.8O0LQ0jgu5bE3t/y7w8mf8/HzVXn8m12G', 'ADMIN'),
    ('killer', 'killer killerovich', '$2a$10$PuLEUApJSoMxQ4vLNDnMSePfhOCdCc83U9W9P77OOTW/GxoJ0wefW', 'KILLER');
