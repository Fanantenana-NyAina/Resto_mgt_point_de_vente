BEGIN;

CREATE TYPE stock_movement_type as ENUM('IN','OUT');
CREATE TYPE unit as ENUM('G','L','U');

COMMIT;

CREATE TABLE IF NOT EXISTS stock_movement(
    id BIGSERIAL PRIMARY KEY,
    id_ingredient int REFERENCES ingredient(id),
    move stock_movement_type NOT NULL,
    quantity DECIMAL NOT NULL,
    unit unit NOT NULL,
    created_datetime timestamp NOT NULL
);