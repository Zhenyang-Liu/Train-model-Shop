CREATE TABLE Roles (
    RoleID INT AUTO_INCREMENT PRIMARY KEY,
    RoleName VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE Permissions (
    PermissionID INT AUTO_INCREMENT PRIMARY KEY,
    PermissionName VARCHAR(255) NOT NULL UNIQUE,
    Description VARCHAR(255)
);

CREATE TABLE Role_Permissions (
    RoleID INT,
    PermissionID INT,
    PRIMARY KEY (RoleID, PermissionID),
    FOREIGN KEY (RoleID) REFERENCES Roles(RoleID),
    FOREIGN KEY (PermissionID) REFERENCES Permissions(PermissionID)
);


CREATE TABLE User (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    Email VARCHAR(255) NOT NULL,
    Password VARCHAR(255) NOT NULL, -- Should Add Encryption Method
    Forename VARCHAR(255),
    Surname VARCHAR(255),
    RoleID INT,
    FOREIGN KEY (RoleID) REFERENCES Roles(RoleID)
);

CREATE TABLE Address (
    HouseNumber VARCHAR(255),
    RoadName VARCHAR(255),
    CityName VARCHAR(255),
    Postcode VARCHAR(10),
    UserID INT,
    FOREIGN KEY (UserID) REFERENCES User(UserID)
);

CREATE TABLE BankDetails (
    CardName VARCHAR(255),
    CardHolderName VARCHAR(255),
    CardNumber VARCHAR(255), -- Should Add Encryption Method
    ExpiryDate DATE,
    SecurityCode VARCHAR(255), -- Should Add Encryption Method
    UserID INT,
    FOREIGN KEY (UserID) REFERENCES User(UserID)
);

CREATE TABLE Orders (
    OrderNumber INT AUTO_INCREMENT PRIMARY KEY,
    Date DATE,
    TotalCost FLOAT,
    Status ENUM('Confirmed', 'Fulfilled', 'Declined'),
    UserID INT,
    FOREIGN KEY (UserID) REFERENCES User(UserID)
);

CREATE TABLE Brand (
    BrandID INT AUTO_INCREMENT PRIMARY KEY,
    BrandName VARCHAR(255),
    Country VARCHAR(255)
);

CREATE TABLE Product (
    ProductID INT AUTO_INCREMENT PRIMARY KEY,
    BrandID INT,
    ProductName VARCHAR(255),
    ProductCode VARCHAR(255),
    RetailPrice FLOAT,
    Description TEXT,
    StockQuantity INT,
    FOREIGN KEY (BrandID) REFERENCES Brand(BrandID)
);

CREATE TABLE OrderLines (
    ProductID INT,
    Quantity INT,
    LineCost FLOAT,
    OrderNumber INT,
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID),
    FOREIGN KEY (OrderNumber) REFERENCES Orders(OrderNumber)
);

CREATE TABLE Era (
    EraCode INT PRIMARY KEY,
    Description VARCHAR(255)
);

CREATE TABLE ProductEra (
    EraCode INT,
    ProductID INT,
    PRIMARY KEY (EraCode, ProductID),
    FOREIGN KEY (EraCode) REFERENCES Era(EraCode),
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);


CREATE TABLE Track (
    ProductID INT,
    TrackType VARCHAR(255),
    Gauge VARCHAR(255),
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);

CREATE TABLE Controller (
    ProductID INT,
    DigitalType BOOLEAN,
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);

CREATE TABLE Locomotive (
    ProductID INT,
    Gauge VARCHAR(255),
    DCCType ENUM('Analogue', 'Ready', 'Fitted', 'Sound'),
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);

CREATE TABLE RollingStock (
    ProductID INT,
    Type ENUM('Carriage', 'Wagon'),
    Gauge VARCHAR(255),
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);

CREATE TABLE BoxedSet (
    ProductID INT,
    PackType VARCHAR(255),
    FOREIGN KEY (ProductID) REFERENCES Product(ProductID)
);

CREATE TABLE BoxedSet_Locomotive (
    BoxedSetID INT,
    LocomotiveID INT,
    Quantity INT,
    PRIMARY KEY (BoxedSetID, LocomotiveID),
    FOREIGN KEY (BoxedSetID) REFERENCES BoxedSet(ProductID),
    FOREIGN KEY (LocomotiveID) REFERENCES Locomotive(ProductID)
);

CREATE TABLE BoxedSet_RollingStock (
    BoxedSetID INT,
    RollingStockID INT,
    Quantity INT,
    PRIMARY KEY (BoxedSetID, RollingStockID),
    FOREIGN KEY (BoxedSetID) REFERENCES BoxedSet(ProductID),
    FOREIGN KEY (RollingStockID) REFERENCES RollingStock(ProductID)
);

CREATE TABLE BoxedSet_Track (
    BoxedSetID INT,
    TrackID INT,
    Quantity INT,
    PRIMARY KEY (BoxedSetID, TrackID),
    FOREIGN KEY (BoxedSetID) REFERENCES BoxedSet(ProductID),
    FOREIGN KEY (TrackID) REFERENCES Track(ProductID)
);