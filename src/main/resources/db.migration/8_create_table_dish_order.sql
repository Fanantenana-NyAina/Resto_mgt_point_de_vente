CREATE TABLE IF NOT EXISTS dish_order(
    id_dish int NOT NULL REFERENCES dish(id),
    id_order int NOT NULL REFERENCES "order"(id),
    quantity int NOT NULL,
    PRIMARY KEY (id_dish,id_order)
);