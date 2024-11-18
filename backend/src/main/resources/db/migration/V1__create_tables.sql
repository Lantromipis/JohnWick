CREATE TABLE "user"
(
    id           UUID PRIMARY KEY,
    username     VARCHAR(63) UNIQUE NOT NULL,
    display_name TEXT               NOT NULL,
    password     TEXT               NOT NULL,
    role         VARCHAR(63)        NOT NULL

);

CREATE TABLE "order"
(
    id                UUID PRIMARY KEY,
    created_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    type              VARCHAR(63)              NOT NULL,
    description       TEXT,
    status            VARCHAR(63)              NOT NULL,
    target_name       TEXT
);

CREATE TABLE head_haunt_order
(
    order_id            UUID PRIMARY KEY NOT NULL REFERENCES "order" (id),
    succeeded_killer_id UUID REFERENCES "user" (id),
    customer_name       TEXT             NOT NULL,
    current_price       BIGINT           NOT NULL
);

CREATE TABLE promissory_note_order
(
    order_id            UUID PRIMARY KEY NOT NULL REFERENCES "order" (id),
    beneficiary_user_id UUID             NOT NULL REFERENCES "user" (id),
    debtor_user_id      UUID             NOT NULL REFERENCES "user" (id)
);

CREATE TABLE regular_order
(
    order_id           UUID PRIMARY KEY NOT NULL REFERENCES "order" (id),
    assigned_killer_id UUID REFERENCES "user" (id),
    price              BIGINT           NOT NULL,
    customer_name      TEXT             NOT NULL
);

CREATE TABLE regular_order_application
(
    id                UUID PRIMARY KEY,
    applied_killer_id UUID                     NOT NULL REFERENCES "user" (id),
    regular_order_id  UUID                     NOT NULL REFERENCES "regular_order" (order_id),
    created_timestamp TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE TABLE notification
(
    id                UUID PRIMARY KEY,
    user_id           UUID                     NOT NULL REFERENCES "user" (id),
    title             TEXT,
    message           TEXT,
    created_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    is_read           BOOLEAN                  NOT NULL
);
CREATE TABLE cleaning_request
(
    id                 UUID PRIMARY KEY,
    order_id           UUID REFERENCES "order" (id),
    requested_by       UUID REFERENCES "user" (id),
    applied_cleaner_id UUID REFERENCES "user" (id),
    created_timestamp  TIMESTAMP WITH TIME ZONE NOT NULL,
    status             VARCHAR(63)              NOT NULL,
    details            TEXT
);
CREATE TABLE appointment_schedule
(
    id           UUID PRIMARY KEY,
    host_user_id UUID REFERENCES "user" (id),
    date         DATE                NOT NULL,
    from_time    TIME WITH TIME ZONE NOT NULL,
    to_time      TIME WITH TIME ZONE NOT NULL
);

CREATE TABLE appointment
(
    id                       UUID PRIMARY KEY,
    booked_by_user_id        UUID                NOT NULL REFERENCES "user" (id),
    appointments_schedule_id UUID                NOT NULL REFERENCES "appointment_schedule" (id) ON DELETE CASCADE,
    from_time                TIME WITH TIME ZONE NOT NULL,
    to_time                  TIME WITH TIME ZONE NOT NULL,
    message                  TEXT
);