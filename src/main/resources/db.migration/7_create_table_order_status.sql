BEGIN;
CREATE TYPE status as enum('CREATED','CONFIRMED','IN_PROGRESS','FINISHED','DELIVERED');
COMMIT;

CREATE TABLE IF NOT EXISTS order_status(
    id BIGSERIAL PRIMARY KEY,
    id_order int NOT NULL REFERENCES "order"(id),
    status status NOT NULL UNIQUE DEFAULT 'CREATED'::status,
    created_datetime timestamp DEFAULT now()
);