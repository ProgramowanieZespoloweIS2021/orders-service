DROP SEQUENCE IF EXISTS orders_id_sequence;
CREATE SEQUENCE IF NOT EXISTS orders_id_sequence
    MINVALUE 1
    START WITH 1
    INCREMENT BY 40;


DROP TABLE IF EXISTS orders;
CREATE TABLE IF NOT EXISTS orders
(
    id            SERIAL PRIMARY KEY,
    buyer_id      INTEGER,
    seller_id     INTEGER,
    offer_id      INTEGER,
    tier_id       INTEGER,
    description   TEXT,
    state         TEXT,
    creation_date TIMESTAMP
);