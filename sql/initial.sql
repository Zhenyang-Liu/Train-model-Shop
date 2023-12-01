CREATE TABLE User (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    forename VARCHAR(100),
    surname VARCHAR(100),
    email VARCHAR(100),

);

CREATE TABLE Login (
    login_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    session_code VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    password_salt VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

CREATE TABLE EncryptionKeys (
    key_id INT AUTO_INCREMENT PRIMARY KEY,
    key_value VARCHAR(255) NOT NULL
);

CREATE TABLE User_Key (
    user_id INT UNIQUE,
    key_id INT,
    PRIMARY KEY (user_id, key_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (key_id) REFERENCES EncryptionKeys(key_id)
);

CREATE TABLE Role (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(10) NOT NULL UNIQUE
);

CREATE TABLE User_Role (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id ON DELETE CASCADE),
    FOREIGN KEY (role_id) REFERENCES Role(role_id)
);

CREATE TABLE Permission (
    permission_id INT AUTO_INCREMENT PRIMARY KEY,
    permission_name VARCHAR(100) NOT NULL
);

CREATE TABLE Role_Permission (
    role_id INT NOT NULL,
    permission_id INT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES Role(role_id),
    FOREIGN KEY (permission_id) REFERENCES Permission(permission_id)
);

CREATE TABLE Address (
    address_id INT AUTO_INCREMENT PRIMARY KEY,
    house_number VARCHAR(255) NOT NULL,
    road_name VARCHAR(255) NOT NULL,
    city_name VARCHAR(255) NOT NULL,
    postcode VARCHAR(10) NOT NULL,
    UNIQUE INDEX unique_address (house_number, postcode)
);

CREATE TABLE User_Address (
    user_id INT NOT NULL,
    address_id INT NOT NULL,
    PRIMARY KEY (user_id, address_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (address_id) REFERENCES Address(address_id)
);

CREATE TABLE Bank_Detail (
    bank_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    card_name VARCHAR(255) NOT NULL,
    card_holder_name VARCHAR(255) NOT NULL,
    card_number VARCHAR(255) NOT NULL, 
    expiry_date VARCHAR(10) NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    delivery_address_id INT,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    total_cost DECIMAL(10, 2) NOT NULL,
    status ENUM('Pending', 'Confirmed', 'Fulfilled', 'Cancelled') NOT NULL;
    bank_detail_state INT NOT NULL,
    reason VARCHAR(255) DEFAULT NULL;
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (delivery_address_id) REFERENCES Address(address_id)
);

CREATE TABLE Cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    creation_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

CREATE TABLE Product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    brand_name VARCHAR(255) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_code VARCHAR(8) NOT NULL,
    retail_price DECIMAL(10, 2) NOT NULL,
    description TEXT NOT NULL,
    stock_quantity INT NOT NULL,
    product_image TEXT
);

CREATE TABLE Order_Line (
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    line_cost DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id)
);

CREATE TABLE Cart_Item (
    cart_item_id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE,
    FOREIGN KEY (cart_id) REFERENCES Cart(cart_id)
);

CREATE TABLE Era (
    era_code INT PRIMARY KEY,
    description VARCHAR(255)
);

CREATE TABLE ProductEra (
    era_code INT,
    product_id INT,
    PRIMARY KEY (era_code, product_id),
    FOREIGN KEY (era_code) REFERENCES Era(era_code),
    FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE
);

CREATE TABLE Track (
    product_id INT,
    gauge VARCHAR(255),
    FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE
);

CREATE TABLE Controller (
    product_id INT,
    digital_type TINYINT(1),
    FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE
);

CREATE TABLE Locomotive (
    product_id INT,
    gauge VARCHAR(255),
    dcc_type ENUM('Analogue', 'Ready', 'Fitted', 'Sound'),
    FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE
);

CREATE TABLE RollingStock (
    product_id INT,
    type ENUM('Carriage', 'Wagon'),
    gauge VARCHAR(255),
    FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE
);

CREATE TABLE BoxedSet (
    product_id INT,
    pack_type VARCHAR(255),
    FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE
);

CREATE TABLE BoxedSet_Item (
    boxed_set_id INT,
    item_id INT,
    quantity INT,
    PRIMARY KEY (boxed_set_id, item_id),
    FOREIGN KEY (boxed_set_id) REFERENCES BoxedSet(product_id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES Product(product_id) ON DELETE CASCADE
);

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