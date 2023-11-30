INSERT INTO Era (era_code, description) VALUES
    (1, 'Era 1: 1804 - 1874 Pioneering'),
    (2, 'Era 2: 1875 - 1922 Pre-Grouping'),
    (3, 'Era 3: 1923 - 1947: The Big Four'),
    (4, 'Era 4: 1948 - 1956: British Railways Steam (Early Crest Livery'),
    (5, 'Era 5: 1957 - 1966: British Railways Steam (Late Crest Livery)'),
    (6, 'Era 6: 1967 - 1971: British Railways Corporate Blue (Pre-TOPS)'),
    (7, 'Era 7: 1972 - 1982: British Railways Corporate Blue (Post-TOPS)'),
    (8, 'Era 8: 1983 - 1994: British Rail Sectorisation'),
    (9, 'Era 9: 1995 - 2004: Initial Privatisation'),
    (10, 'Era 10: 2005 - 2015: Rebuilding of the Railways'),
    (11, 'Era 11: 2016 - 2026: Current Era');

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
    ('MANAGE_PRODUCTS'),
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
    (2,12),
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
    (3,12),
    (3,13);