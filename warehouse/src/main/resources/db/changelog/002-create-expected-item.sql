--liquibase formatted sql

--changeset devforge:2
CREATE TABLE expected_item
(
    id                   UUID PRIMARY KEY,
    expected_delivery_id UUID           NOT NULL,
    name                 VARCHAR(255)   NOT NULL,
    quantity             NUMERIC(12, 3) NOT NULL,

    CONSTRAINT fk_expected_item_delivery
        FOREIGN KEY (expected_delivery_id)
            REFERENCES expected_delivery (id)
);