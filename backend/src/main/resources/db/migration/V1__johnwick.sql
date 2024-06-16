CREATE TABLE application_user
(
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

INSERT INTO application_user (username, display_name, password, role) VALUES
    ('admin', 'admin adminovich', '$2a$10$/FarO5LVt.6SAUGBlYf.8O0LQ0jgu5bE3t/y7w8mf8/HzVXn8m12G', 'ADMIN'),
    ('killer', 'killer killerovich', '$2a$10$PuLEUApJSoMxQ4vLNDnMSePfhOCdCc83U9W9P77OOTW/GxoJ0wefW', 'KILLER');
