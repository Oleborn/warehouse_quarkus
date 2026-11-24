--liquibase formatted sql

--changeset devforge:4
CREATE TABLE received_item
(
    id               UUID PRIMARY KEY,
    receipt_id       UUID NOT NULL,
    expected_item_id UUID,
    quantity         NUMERIC(12, 3) NOT NULL,

    CONSTRAINT fk_received_item_receipt
        FOREIGN KEY (receipt_id)
            REFERENCES receipt (id),

    CONSTRAINT fk_received_item_expected_item
        FOREIGN KEY (expected_item_id)
            REFERENCES expected_item (id)
);