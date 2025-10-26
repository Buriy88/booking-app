DELETE FROM users;
ALTER TABLE users ALTER COLUMN id RESTART WITH 1001;


INSERT INTO users (id, email, first_name, last_name, password, role)
VALUES
    (1001, 'customer@example.com', 'Cust', 'Omer', '{noop}123456', 'ROLE_CUSTOMER'),
    (1002, 'manager@example.com',  'Man',  'Ager', '{noop}123456', 'ROLE_MANAGER');

