CREATE TABLE IF NOT EXISTS price (
    id SERIAL PRIMARY KEY,
    id_ingredient INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    created_datetime timestamp NOT NULL DEFAULT now(),
    FOREIGN KEY (id_ingredient) REFERENCES ingredient(id)
);
