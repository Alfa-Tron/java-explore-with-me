CREATE SCHEMA IF NOT EXISTS public;
CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
     );

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    CONSTRAINT uq_email UNIQUE (email)
    );

CREATE TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title  VARCHAR(512) NOT NULL,
    pinned BOOLEAN      NOT NULL
    );

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation         VARCHAR(2000)               NOT NULL,
    category_id        BIGINT                      NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description        VARCHAR(7000)               NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator_id       BIGINT                      NOT NULL,
    location_id        BIGINT                      NOT NULL,
    paid               BOOLEAN                     NOT NULL,
    participant_limit  INTEGER                     NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN                     NOT NULL,
    state              varchar(50)                 NOT NULL,
    title              VARCHAR(120)                NOT NULL,

    CONSTRAINT events_categories_fk FOREIGN KEY (category_id) REFERENCES categories,
    CONSTRAINT events_users_fk FOREIGN KEY (initiator_id) REFERENCES users,
    CONSTRAINT events_locations_fk FOREIGN KEY (location_id) REFERENCES locations
    );

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_id     BIGINT                      NOT NULL,
    requester_id BIGINT                      NOT NULL,
    status       VARCHAR(50)                 NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE NOT NULL NOT NULL,
    CONSTRAINT requests_users_fk FOREIGN KEY (requester_id) REFERENCES users,
    CONSTRAINT requests_events_fk FOREIGN KEY (event_id) REFERENCES events
    );

CREATE TABLE IF NOT EXISTS compilation_events
(
    compilation_id BIGINT NOT NULL,
    event_id       BIGINT NOT NULL,
    PRIMARY KEY (compilation_id, event_id),
    CONSTRAINT compilation_events_compilations_fk FOREIGN KEY (compilation_id) references users,
    CONSTRAINT compilation_events_events_fk FOREIGN KEY (event_id) references events
    );
CREATE TABLE IF NOT EXISTS comments
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text     VARCHAR                     NOT NULL,
    user_id  BIGINT                      NOT NULL REFERENCES users (id),
    event_id BIGINT                      NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    created  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    modified TIMESTAMP WITHOUT TIME ZONE
    )