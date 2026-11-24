--liquibase formatted sql

--changeset devforge:3
CREATE TABLE receipt
(
    id                       UUID PRIMARY KEY,
    number                   VARCHAR(255) UNIQUE NOT NULL,
    expected_delivery_number VARCHAR(255)        NOT NULL,
    received_at              DATE                NOT NULL,

    CONSTRAINT fk_receipt_expected_delivery
        FOREIGN KEY (expected_delivery_number)
            REFERENCES expected_delivery (number)
);