INSERT INTO Roles (role) VALUES ('ADMIN')
INSERT INTO Roles (role) VALUES ('BASIC')
INSERT INTO Roles (role) VALUES ('USER_MNGR')
INSERT INTO Roles (role) VALUES ('SUBSCRIPTION_MNGR')
INSERT INTO Roles (role) VALUES ('DEVICE_MNGR')
INSERT INTO Roles (role) VALUES ('FINANCE_MNGR')

INSERT INTO Users (email, password, full_name, PASSWORD_RENEWER_KEY, enabled, status) VALUES ('admin', 'admin', 'admin', null, true, 2)

INSERT INTO Users_roles (user_id, role_id) VALUES (1, 1)
INSERT INTO Users_roles (user_id, role_id) VALUES (1, 2)

INSERT INTO Sims (imei, pin, puk, status) VALUES ('8936300123456789101', '0123', '0123456789', 0)

INSERT INTO Device_types (brand, model, name, sim_number, microsd, visible) VALUES ('Samsung', 'S10', 'Samsung S10', 2, true, true)
INSERT INTO Device_types (brand, model, name, sim_number, microsd, visible) VALUES ('Motorola', 'C380', 'Motorola C380', 1, false, false)

INSERT INTO Categories (name) VALUES ('Monthly')
INSERT INTO Categories (name) VALUES ('Call')
INSERT INTO Categories (name) VALUES ('Internet')

INSERT INTO description_category_coupler(name, available) VALUES ('default', true)

INSERT INTO description_category_map (description_category_coupler_id, description_category_map_id, description_category_map_key) VALUES (1, 1, 'Mobil telefon szolgaltatas')
INSERT INTO description_category_map (description_category_coupler_id, description_category_map_id, description_category_map_key) VALUES (1, 2, 'Telekom mobilhalozaton belul')
INSERT INTO description_category_map (description_category_coupler_id, description_category_map_id, description_category_map_key) VALUES (1, 2, 'Belfoldi mas mobilhalozat / Telenor')
INSERT INTO description_category_map (description_category_coupler_id, description_category_map_id, description_category_map_key) VALUES (1, 2, 'Belfoldi mas mobilhalozat / Vodafone')
INSERT INTO description_category_map (description_category_coupler_id, description_category_map_id, description_category_map_key) VALUES (1, 3, 'Mobil net 1GB')


