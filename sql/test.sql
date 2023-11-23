INSERT INTO Era (era_code, description) VALUES
    (1, 'Era 1'),
    (2, 'Era 2'),
    (3, 'Era 3'),
    (4, 'Era 4'),
    (5, 'Era 5'),
    (6, 'Era 6'),
    (7, 'Era 7'),
    (8, 'Era 8'),
    (9, 'Era 9'),
    (10, 'Era 10'),
    (11, 'Era 11');

INSERT INTO Brand (country, brand_name) VALUES ('UK','Bachmann');

INSERT INTO Role (role_name) VALUES
    ('CUSTOMER'),
    ('STAFF'),
    ('MANAGER');

INSERT INTO Permission (permission_name) VALUES
    ('BROWSE_PRODUCTS'),
    ('EDIT_OWN_EMAIL'),
    ('EDIT_OWN_ADDRESS'),
    ('EDIT_OWN_BANK_DETAILS'),
    ('EDIT_OWN_PASSWORD'),
    ('EDIT_OWN_CART'),
    ('CREATE_ORDER'),
    ('VIEW_OWN_ORDERS'),
    ('UPDATE_PRODUCT'),
    ('VIEW_CUSTOMER_DETAILS'),
    ('MANAGE_ORDERS'),
    ('ASSIGN_STAFF_ROLE');

INSERT INTO Role_Permission (role_id, permission_id) VALUES
    (1,1),
    (1,2),
    (1,3),
    (1,4),
    (1,5),
    (1,6),
    (1,7),
    (1,8),
    (2,1),
    (2,2),
    (2,3),
    (2,4),
    (2,5),
    (2,6),
    (2,7),
    (2,8),
    (2,9),
    (2,10),
    (2,11),
    (3,1),
    (3,2),
    (3,3),
    (3,4),
    (3,5),
    (3,6),
    (3,7),
    (3,8),
    (3,9),
    (3,10),
    (3,11),
    (3,12);