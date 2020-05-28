INSERT INTO Roles (role) VALUES ('ADMIN')

INSERT INTO Users (email, password, full_name, enabled) VALUES ('admin', 'admin', 'Admin', true)

INSERT INTO Device_types (brand, model, name, sim_number, microsd, visible) VALUES ('Samsung', 'S10', 'Samsung S10', 1, true, true)
INSERT INTO Device_types (brand, model, name, sim_number, microsd, visible) VALUES ('Huawei', 'Mate', 'Huawei Mate', 1, true, true)

INSERT INTO Devices (serial_number, type_id) VALUES ('sn1', 1)
INSERT INTO Devices (serial_number, type_id) VALUES ('sn2', 2)

INSERT INTO Subscriptions(number, device_id, last) VALUES ('201234567', 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO Subscriptions(number, device_id, last) VALUES ('207654321', null, to_date('20-01-01', 'RR-MM-DD'))

INSERT INTO Sims (imei, service_provider) VALUES ('imei1', 'Telekom')
INSERT INTO Sims (imei, service_provider) VALUES ('imei2', 'Telekom')
INSERT INTO Sims (imei, service_provider) VALUES ('imei3', 'Telekom')

INSERT INTO Sub_note (note, sub_id, date) VALUES ('', 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO Sub_note (note, sub_id, date) VALUES ('HEHE', 2, to_date('20-01-01', 'RR-MM-DD'))

INSERT INTO Sim_status (sim_id, connect, status) VALUES (1, to_date('20-01-01', 'RR-MM-DD'), 1)
INSERT INTO Sim_status (sim_id, connect, status) VALUES (2, to_date('20-01-01', 'RR-MM-DD'), 1)
INSERT INTO Sim_status (sim_id, connect, status) VALUES (3, to_date('20-01-01', 'RR-MM-DD'), 0)

INSERT INTO Users_roles (user_id, role_id) VALUES (1, 1)

INSERT INTO User_sub_st (user_id, sub_id, connect) VALUES (1, 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO User_sub_st (user_id, sub_id, connect) VALUES (1, 2, to_date('20-01-01', 'RR-MM-DD'))

INSERT INTO Sub_sim_st (sub_id, sim_id, connect) VALUES (1, 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO Sub_sim_st (sub_id, sim_id, connect) VALUES (2, 2, to_date('20-01-01', 'RR-MM-DD'))

INSERT INTO User_dev_st (user_id, dev_id, connect) VALUES (1, 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO User_dev_st (user_id, dev_id, connect) VALUES (1, 2, to_date('20-01-01', 'RR-MM-DD'))

INSERT INTO Sub_dev_st (sub_id, dev_id, connect) VALUES (1, 1, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO Sub_dev_st (sub_id, dev_id, connect) VALUES (null, 2, to_date('20-01-01', 'RR-MM-DD'))
INSERT INTO Sub_dev_st (sub_id, dev_id, connect) VALUES (2, null, to_date('20-01-01', 'RR-MM-DD'))

INSERT INTO Categories (name) VALUES ('Monthly')
INSERT INTO Categories (name) VALUES ('Call')
INSERT INTO Categories (name) VALUES ('Internet')

INSERT INTO Pay_devision (available, name) VALUES (true, 'basic')

INSERT INTO Pay_devision_category_ratio (pay_devision_id, category_ratio_key, category_ratio) VALUES (1, 'Call' , 50)
INSERT INTO Pay_devision_category_ratio (pay_devision_id, category_ratio_key, category_ratio) VALUES (1, 'Monthly' , 0)
INSERT INTO Pay_devision_category_ratio (pay_devision_id, category_ratio_key, category_ratio) VALUES (1, 'Internet' , 30)

INSERT INTO Users_pay_devs (user_id, pay_devs_id) VALUES (1, 1)
