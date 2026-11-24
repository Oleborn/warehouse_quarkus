--liquibase formatted sql

--changeset devforge:1
CREATE TABLE expected_delivery
(
    id            UUID PRIMARY KEY,
    number        VARCHAR(255) UNIQUE NOT NULL,
    supplier      VARCHAR(255),
    expected_date DATE,
    status        VARCHAR(50)
);