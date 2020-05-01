INSERT INTO Roles (role) VALUES ('ADMIN')
INSERT INTO Roles (role) VALUES ('USER')
INSERT INTO Roles (role) VALUES ('GUEST')

INSERT INTO Users (email, password, full_name, enabled) VALUES ('admin', 'admin', 'Admin', true)
INSERT INTO Users (email, password, full_name, enabled) VALUES ('user', 'user', 'User', true)
INSERT INTO Users (email, password, full_name, enabled) VALUES ('f', 'f', 'False', false)

INSERT INTO Subscriptions(number) VALUES ('202563364')
INSERT INTO Subscriptions(number) VALUES ('number2')
INSERT INTO Subscriptions(number) VALUES ('number3')

INSERT INTO Sims (imei, service_provider) VALUES ('imei1', 'Telekom')
INSERT INTO Sims (imei, service_provider) VALUES ('imei2', 'Telekom')
INSERT INTO Sims (imei, service_provider) VALUES ('imei3', 'Telekom')
INSERT INTO Sims (imei, service_provider) VALUES ('f1', 'FREE')
INSERT INTO Sims (imei, service_provider) VALUES ('f2', 'FREE')

INSERT INTO Sub_note (note, sub_id, date) VALUES ('', 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO Sub_note (note, sub_id, date) VALUES ('', 2, to_date('20-01-02', 'RR-MM-DD'))
INSERT INTO Sub_note (note, sub_id, date) VALUES ('', 3, to_date('20-01-03', 'RR-MM-DD'))

INSERT INTO Sim_status (sim_id, connect, status) VALUES (1, to_date('20-01-01', 'RR-MM-DD'), 1)
INSERT INTO Sim_status (sim_id, connect, status) VALUES (2, to_date('20-01-02', 'RR-MM-DD'), 1)
INSERT INTO Sim_status (sim_id, connect, status) VALUES (3, to_date('20-01-03', 'RR-MM-DD'), 1)
INSERT INTO Sim_status (sim_id, connect, status) VALUES (4, to_date('20-01-03', 'RR-MM-DD'), 0)
INSERT INTO Sim_status (sim_id, connect, status) VALUES (5, to_date('20-01-03', 'RR-MM-DD'), 0)

INSERT INTO Device_types (brand, model, name, sim_number, microsd, visible) VALUES ('Samsung', 'S10', 'Samsung S10', 2, true, true)
INSERT INTO Device_types (brand, model, name, sim_number, microsd, visible) VALUES ('Samsung', 'A71', 'Samsung A71', 2, true, true)
INSERT INTO Device_types (brand, model, name, sim_number, microsd, visible) VALUES ('Huawie', 'Mate 30', 'Huawie Mate 30', 1, true, true)

INSERT INTO Devices (serial_number, type_id) VALUES ('sn1', 1)
INSERT INTO Devices (serial_number, type_id) VALUES ('sn2', 2)
INSERT INTO Devices (serial_number, type_id) VALUES ('sn3', 3)

INSERT INTO Users_roles (user_id, role_id) VALUES (1, 1)
INSERT INTO Users_roles (user_id, role_id) VALUES (2, 2)

INSERT INTO User_sub_st (user_id, sub_id, connect) VALUES (1, 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO User_sub_st (user_id, sub_id, connect) VALUES (2, 2, to_date('20-01-02', 'RR-MM-DD'))
INSERT INTO User_sub_st (user_id, sub_id, connect) VALUES (3, 3, to_date('20-01-03', 'RR-MM-DD'))

INSERT INTO Sub_sim_st (sub_id, sim_id, connect) VALUES (1, 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO Sub_sim_st (sub_id, sim_id, connect) VALUES (2, 2, to_date('20-01-02', 'RR-MM-DD'))
INSERT INTO Sub_sim_st (sub_id, sim_id, connect) VALUES (3, 3, to_date('20-01-03', 'RR-MM-DD'))

INSERT INTO User_dev_st (user_id, dev_id, connect) VALUES (1, 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO User_dev_st (user_id, dev_id, connect) VALUES (2, 2, to_date('20-01-02', 'RR-MM-DD'))
INSERT INTO User_dev_st (user_id, dev_id, connect) VALUES (3, 3, to_date('20-01-03', 'RR-MM-DD'))

INSERT INTO Sub_dev_st (sub_id, dev_id, connect) VALUES (1, 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO Sub_dev_st (sub_id, dev_id, connect) VALUES (2, 2, to_date('20-01-02', 'RR-MM-DD'))
INSERT INTO Sub_dev_st (sub_id, dev_id, connect) VALUES (3, 3, to_date('20-01-03', 'RR-MM-DD'))

INSERT INTO Categories (name) VALUES ('Monthly')
INSERT INTO Categories (name) VALUES ('Call')
INSERT INTO Categories (name) VALUES ('Internet')
INSERT INTO Categories (name) VALUES ('Roaming')
