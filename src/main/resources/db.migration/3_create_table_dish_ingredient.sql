CREATE TABLE IF NOT EXISTS dish_ingredient(
      id_dish INT REFERENCES dish(id),
      id_ingredient INT REFERENCES ingredient(id),
      required_quantity DECIMAL NOT NULL,
      unit unit NOT NULL,
      CONSTRAINT dish_ingredient_pk PRIMARY KEY (id_dish,id_ingredient)
);