
CREATE TABLE IF NOT EXISTS "user"
(
                                     id UUID PRIMARY KEY,
                                     username VARCHAR(63) UNIQUE NOT NULL,
                                     display_name TEXT NOT NULL,
                                     password TEXT NOT NULL,
                                     role VARCHAR(63) NOT NULL,
                                     status VARCHAR(63) NOT NULL
);
INSERT INTO "user" (id,username, display_name, password, role,status) VALUES
                                                                (gen_random_uuid(),'admin', 'admin adminovich', '$2a$10$/FarO5LVt.6SAUGBlYf.8O0LQ0jgu5bE3t/y7w8mf8/HzVXn8m12G', 'ADMIN','ACTIVE'),
                                                                (gen_random_uuid(),'killer', 'killer killerovich', '$2a$10$PuLEUApJSoMxQ4vLNDnMSePfhOCdCc83U9W9P77OOTW/GxoJ0wefW', 'KILLER','ACTIVE');
CREATE TABLE IF NOT EXISTS "order"
(
                                      id UUID PRIMARY KEY,
                                      created_timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
                                      type VARCHAR(63) NOT NULL,
                                      description TEXT NOT NULL,
                                      status VARCHAR(63) NOT NULL,
                                      target_name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS head_haunt_order
(
                                                order_id UUID PRIMARY KEY NOT NULL,
                                                succeded_killer_id UUID NOT NULL,
                                                customer_name TEXT NOT NULL,
                                                current_price DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS promissory_note_order
(
                                                     order_id UUID PRIMARY KEY NOT NULL,
                                                     beneficiary_user_id UUID NOT NULL,
                                                     deptor_user_id UUID NOT NULL
);

CREATE TABLE IF NOT EXISTS regular_order
(
                                             order_id UUID PRIMARY KEY,
                                             assigned_killer_id UUID NOT NULL,
                                             price DOUBLE PRECISION NOT NULL,
                                             customer_name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS regular_order_application
(
                                                         id UUID PRIMARY KEY,
                                                         applied_killer_id UUID NOT NULL,
                                                         regular_order_id UUID NOT NULL,
                                                         created_timestamp TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE TABLE IF NOT EXISTS notification
(
                                            id UUID PRIMARY KEY,
                                            user_id UUID,
                                            title TEXT,
                                            message TEXT,
                                            created_timestamp TIMESTAMP WITH TIME ZONE,
                                            is_read BOOLEAN
);
CREATE TABLE IF NOT EXISTS cleaning_request
(
                                                id UUID PRIMARY KEY,
                                                order_id UUID,
                                                requested_by UUID,
                                                applied_cleaner_id UUID,
                                                created_timestamp TIMESTAMP WITH TIME ZONE,
                                                price DOUBLE PRECISION,
                                                status VARCHAR(63),
                                                details TEXT
);
CREATE TABLE IF NOT EXISTS appointment_schedule
(
                                                     id UUID PRIMARY KEY,
                                                     host_user_id UUID,
                                                     date DATE,
                                                     from_time TIME WITH TIME ZONE not null,
                                                     to_time TIME WITH TIME ZONE not null
);

CREATE TABLE IF NOT EXISTS appointment
(
                                           id UUID PRIMARY KEY,
                                           booked_by_user_id UUID,
                                           appointment_schedule_id UUID,
                                           date DATE,
                                           from_timestamp TIME WITH TIME ZONE not null,
                                           to_timestamp TIME WITH TIME ZONE not null,
                                           message TEXT
);

ALTER TABLE if exists head_haunt_order
    ADD FOREIGN KEY (succeded_killer_id) REFERENCES "user" (id),
    ADD FOREIGN KEY (order_id) REFERENCES "order"(id);


ALTER TABLE if exists promissory_note_order
    ADD FOREIGN KEY (order_id) REFERENCES "order"(id),
    ADD FOREIGN KEY (beneficiary_user_id) REFERENCES "user"(id),
    ADD FOREIGN KEY (deptor_user_id) REFERENCES "user"(id);

ALTER TABLE if exists regular_order
    ADD FOREIGN KEY (order_id) REFERENCES "order"(id),
    ADD FOREIGN KEY (assigned_killer_id) REFERENCES "user"(id);

ALTER TABLE if exists regular_order_application
    ADD FOREIGN KEY (regular_order_id) REFERENCES "regular_order"(order_id) ,
    ADD FOREIGN KEY (applied_killer_id) REFERENCES "user"(id);

ALTER TABLE if exists cleaning_request
    ADD FOREIGN KEY (order_id) REFERENCES "order"(id),
    ADD FOREIGN KEY (requested_by) REFERENCES "user"(id),
    ADD FOREIGN KEY (applied_cleaner_id) REFERENCES "user"(id);

ALTER TABLE if exists notification
    ADD FOREIGN KEY (user_id) REFERENCES "user"(id);

ALTER TABLE if exists appointment_schedule
    ADD FOREIGN KEY (host_user_id) REFERENCES "user"(id);

ALTER TABLE if exists appointment
    ADD FOREIGN KEY (booked_by_user_id) REFERENCES "user"(id),
    ADD FOREIGN KEY (appointment_schedule_id) REFERENCES "appointment_schedule"(id);