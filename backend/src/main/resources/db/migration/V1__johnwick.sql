CREATE TABLE "user"
(
    id BIGSERIAL PRIMARY KEY,
    username TEXT UNIQUE NOT NULL,
    display_name TEXT NOT NULL,
    password TEXT NOT NULL,
    role TEXT NOT NULL
);

CREATE TABLE "order"
(
    id BIGSERIAL PRIMARY KEY,
    type TEXT NOT NULL,
    assignee_id BIGINT REFERENCES "user"(id),
    debtor_id BIGINT REFERENCES "user"(id),
    customer TEXT NOT NULL,
    target TEXT,
    price BIGINT NOT NULL,
    description TEXT,
    canceled BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE order_application
(
    id BIGSERIAL PRIMARY KEY,
    applied_killer_id BIGINT NOT NULL REFERENCES "user"(id),
    order_id BIGINT NOT NULL REFERENCES "order"(id)
);

INSERT INTO "user" (username, display_name, password, role) VALUES
    ('admin', 'admin adminovich', '$2a$10$/FarO5LVt.6SAUGBlYf.8O0LQ0jgu5bE3t/y7w8mf8/HzVXn8m12G', 'ADMIN'),
    ('killer', 'killer killerovich', '$2a$10$PuLEUApJSoMxQ4vLNDnMSePfhOCdCc83U9W9P77OOTW/GxoJ0wefW', 'KILLER');
