CREATE TABLE IF NOT EXISTS orders(
    id SERIAL PRIMARY KEY,
    buyer_id INTEGER,
    offer_id INTEGER,
    tier_id INTEGER,
    description TEXT,
    state TEXT,
    creation_date TIMESTAMP
);