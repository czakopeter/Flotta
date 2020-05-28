INSERT INTO Roles (role) VALUES ('ADMIN')

INSERT INTO Users (email, password, full_name, enabled) VALUES ('admin', 'admin', 'Admin', true)

INSERT INTO Device_types (brand, model, name, visible) VALUES ('Samsung', 'A71', 'Samsung A71', true)
INSERT INTO Device_types (brand, model, name, visible) VALUES ('Samsung', 'S20+', 'Samsung S20+', true)
INSERT INTO Device_types (brand, model, name, visible) VALUES ('Huawei', 'P30', 'Huawei P30', false)

INSERT INTO Subscriptions (create_date, last, number, user_id) VALUES (current_date(), current_date(), '201234567', 1)

INSERT INTO Sims (imei, subscription_id, pin, puk) VALUES ('imei1234567', 1, '1234', '1234567890')


INSERT INTO Devices (last, serial_number, type_id, user_id) VALUES (current_date(), '123456789101112A', 1, 1)

