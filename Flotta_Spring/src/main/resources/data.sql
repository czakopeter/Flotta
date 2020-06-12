INSERT INTO Roles (role) VALUES ('ADMIN')
INSERT INTO Roles (role) VALUES ('BASIC')
INSERT INTO Roles (role) VALUES ('USER')
INSERT INTO Roles (role) VALUES ('MOBILE')
INSERT INTO Roles (role) VALUES ('FINANCE')

INSERT INTO Users (email, password, full_name, PASSWORD_RENEWER_KEY ,enabled, status) VALUES ('a', 'a', 'a', null ,true, 2)
INSERT INTO Users (email, password, full_name, PASSWORD_RENEWER_KEY ,enabled, status) VALUES ('basic@gmail.com', 'basic', 'Basic', null ,true, 2)
INSERT INTO Users (email, password, full_name, PASSWORD_RENEWER_KEY ,enabled, status) VALUES ('user@gmail.com', 'user', 'User', null ,true, 2)
INSERT INTO Users (email, password, full_name, PASSWORD_RENEWER_KEY ,enabled, status) VALUES ('mobile@gmail.com', 'mobile', 'Mobile', null ,true, 2)
INSERT INTO Users (email, password, full_name, PASSWORD_RENEWER_KEY ,enabled, status) VALUES ('finance@gmail.com', 'finance', 'Finance', null ,true, 2)
INSERT INTO Users (email, password, full_name, PASSWORD_RENEWER_KEY ,enabled, status) VALUES ('n, 'n', 'n', null ,true, 2)


INSERT INTO Users_roles (user_id, role_id) VALUES (1, 1)
INSERT INTO Users_roles (user_id, role_id) VALUES (1, 2)

INSERT INTO Users_roles (user_id, role_id) VALUES (2, 2)

INSERT INTO Users_roles (user_id, role_id) VALUES (3, 2)
INSERT INTO Users_roles (user_id, role_id) VALUES (3, 3)

INSERT INTO Users_roles (user_id, role_id) VALUES (4, 2)
INSERT INTO Users_roles (user_id, role_id) VALUES (4, 4)

INSERT INTO Users_roles (user_id, role_id) VALUES (5, 2)
INSERT INTO Users_roles (user_id, role_id) VALUES (5, 5)