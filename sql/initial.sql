CREATE TABLE User (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    forename VARCHAR(100),
    surname VARCHAR(100),
    address_id INT
);

CREATE TABLE Login (
    login_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    session_code VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    password_salt VARCHAR(255),
    failed_attempts INT DEFAULT 0,
    last_login_attempt TIMESTAMP,
    lockout_enabled BOOLEAN DEFAULT FALSE,
    lockout_end TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

CREATE TABLE Role (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE User_Role (
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (role_id) REFERENCES Role(role_id)
);

CREATE TABLE Permission (
    permission_id INT AUTO_INCREMENT PRIMARY KEY,
    permission_name VARCHAR(100) NOT NULL
);

CREATE TABLE Role_Permission (
    role_id INT,
    permission_id INT,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES Role(role_id),
    FOREIGN KEY (permission_id) REFERENCES Permission(permission_id)
);

CREATE TABLE Address (
    house_number VARCHAR(255),
    road_name VARCHAR(255),
    city_name VARCHAR(255),
    postcode VARCHAR(10),
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

CREATE TABLE Bank_Detail (
    card_name VARCHAR(255),
    card_holder_name VARCHAR(255),
    card_number VARCHAR(255), 
    expiry_date DATE,
    security_code VARCHAR(255),
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    total_cost FLOAT,
    status ENUM('Pending', 'Confirmed', 'Fulfilled','Declined') NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

CREATE TABLE Cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    creation_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

CREATE TABLE Brand (
    brand_id INT AUTO_INCREMENT PRIMARY KEY,
    brand_name VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL
);

CREATE TABLE Product (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    brand_id INT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_code VARCHAR(8) NOT NULL,
    retail_price FLOAT NOT NULL,
    description TEXT NOT NULL,
    stock_quantity INT NOT NULL,
    FOREIGN KEY (brand_id) REFERENCES Brand(brand_id)
);

CREATE TABLE Order_Line (
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    line_cost FLOAT,
    order_id INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES Product(product_id),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id)
);

CREATE TABLE Cart_Item (
    cart_item_id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT,
    product_id INT,
    quantity INT,
    added_date TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES Product(product_id),
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
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

CREATE TABLE Track (
    product_id INT,
    track_type VARCHAR(255),
    gauge VARCHAR(255),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

CREATE TABLE Controller (
    product_id INT,
    digital_type TINYINT(1),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

CREATE TABLE Locomotive (
    product_id INT,
    gauge VARCHAR(255),
    dcc_type ENUM('Analogue', 'Ready', 'Fitted', 'Sound'),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

CREATE TABLE RollingStock (
    product_id INT,
    type ENUM('Carriage', 'Wagon'),
    gauge VARCHAR(255),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

CREATE TABLE BoxedSet (
    product_id INT,
    pack_type VARCHAR(255),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

CREATE TABLE BoxedSet_Item (
    boxed_set_id INT,
    item_id INT,
    quantity INT,
    PRIMARY KEY (boxed_set_id, item_id),
    FOREIGN KEY (boxed_set_id) REFERENCES BoxedSet(product_id),
    FOREIGN KEY (item_id) REFERENCES Product(product_id)
);