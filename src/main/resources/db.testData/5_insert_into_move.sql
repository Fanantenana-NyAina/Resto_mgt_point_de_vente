INSERT INTO stock_movement (id_ingredient,move,quantity,unit,created_datetime)
VALUES
    (4,'IN',50,'U','2025-02-01 08:00:00'),
    (3,'IN',100,'U','2025-02-01 08:00:00'),
    (2,'IN',20,'L','2025-02-01 08:00:00'),
    (1,'IN',10000,'G','2025-02-01 08:00:00') ON CONFLICT DO NOTHING ;
