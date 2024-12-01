INSERT INTO "user" (id, username, display_name, password, role)
VALUES (random_uuid(), 'admin', 'admin adminovich', '$2a$10$/FarO5LVt.6SAUGBlYf.8O0LQ0jgu5bE3t/y7w8mf8/HzVXn8m12G',
        'ADMIN'),
       (random_uuid(), 'killer', 'killer killerovich',
        '$2a$10$PuLEUApJSoMxQ4vLNDnMSePfhOCdCc83U9W9P77OOTW/GxoJ0wefW', 'KILLER'),
       (random_uuid(), 'tailor', 'tailor tailorovich',
        '$2a$10$BgcgyQrgsu0ikTGyls16DO82QHsntj9/usCzq7Z9SlAmvlM6B4X0K', 'TAILOR'),
       (random_uuid(), 'sommelier', 'sommelier sommelierovich',
        '$2a$10$aThS1WuNwMp.mMQ24hNqJ.LkBX92H6xJ1a1nEw0l3I7Dis6NbZWlu', 'SOMMELIER'),
       (random_uuid(), 'cleaner', 'cleaner cleanerovich',
        '$2a$12$6w.4KSLZcSa128ZlccU1x..eBxpMFwtdiHjN2.rj3CZGYdZtawLfC', 'CLEANER');