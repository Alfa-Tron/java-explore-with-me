DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
CREATE TABLE IF NOT EXISTS endpoint
(
    id
    BIGSERIAL
    PRIMARY
    KEY
    NOT
    NULL,
    app
    VARCHAR
(
    50
) NOT NULL,
    uri VARCHAR
(
    50
) NOT NULL,
    ip VARCHAR
(
    50
) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE not null
    );
