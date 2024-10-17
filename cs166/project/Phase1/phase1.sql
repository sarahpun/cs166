DROP TABLE IF EXISTS Users CASCADE;
DROP TABLE IF EXISTS Catalog CASCADE;
DROP TABLE IF EXISTS RentalOrder CASCADE;
DROP TABLE IF EXISTS TrackingInfo CASCADE;
DROP TABLE IF EXISTS views CASCADE;
DROP TABLE IF EXISTS updates CASCADE;
DROP TABLE IF EXISTS contains CASCADE;
DROP TABLE IF EXISTS Worker CASCADE;
DROP TABLE IF EXISTS Customer CASCADE;

CREATE TABLE Users (
    login CHAR(15) NOT NULL,
    password CHAR(50) NOT NULL,
    phoneNumber CHAR(60) NOT NULL,
    role CHAR(10) NOT NULL,
    favGames CHAR(500),
    numOverdueGames INT NOT NULL,
    PRIMARY KEY(login)
);

CREATE TABLE Catalog (
    gameName CHAR(50) NOT NULL,
    genre CHAR(40) NOT NULL,
    price FLOAT NOT NULL,
    description CHAR(600),
    imageURL CHAR(256),
    PRIMARY KEY (gameName)
);

CREATE TABLE RentalOrder (
    rentalOrderID CHAR(60) NOT NULL,
    login CHAR(50) NOT NULL,
    orderTimestamp TIMESTAMP NOT NULL,
    dueDate DATE NOT NULL,
    totalPrice FLOAT NOT NULL,
    PRIMARY KEY (rentalOrderID),
    FOREIGN KEY (login) REFERENCES Users(login) ON DELETE CASCADE
);

CREATE TABLE TrackingInfo (
    trackingID CHAR(50) NOT NULL,
    status CHAR(40) NOT NULL,
    currentLocation CHAR(60) NOT NULL,
    lastUpdateDate TIMESTAMP NOT NULL,
    rentalOrderID CHAR(50) NOT NULL,
    courierName CHAR(800) NOT NULL,
    additionalComments TEXT,
    PRIMARY KEY (trackingID),
    FOREIGN KEY (rentalOrderID) REFERENCES RentalOrder(rentalOrderID) ON DELETE CASCADE
);

CREATE TABLE views(
    login CHAR(15) NOT NULL,
    gameName CHAR(50) NOT NULL,
    PRIMARY KEY (login, gameName),
    FOREIGN KEY(login) REFERENCES Users(login) ON DELETE CASCADE,
    FOREIGN KEY(gameName) REFERENCES Catalog(gameName) ON DELETE CASCADE
);

CREATE TABLE updates(
    login CHAR(15) NOT NULL,
    gameName CHAR(50) NOT NULL,
    trackingID CHAR(50) NOT NULL,
    rentalOrderID CHAR(60) NOT NULL,
    PRIMARY KEY(login, gameName, trackingID, rentalOrderID),
    FOREIGN KEY(login) REFERENCES Users(login) ON DELETE CASCADE,
    FOREIGN KEY(gameName) REFERENCES Catalog(gameName) ON DELETE CASCADE,
    FOREIGN KEY(trackingID) REFERENCES TrackingInfo(trackingID) ON DELETE CASCADE,
    FOREIGN KEY(rentalOrderID) REFERENCES RentalOrder(rentalOrderID) ON DELETE CASCADE
);

CREATE TABLE contains(
    trackingID CHAR(50) NOT NULL,
    rentalOrderID CHAR(60) NOT NULL,
    PRIMARY KEY(trackingID, rentalOrderID),
    FOREIGN KEY(trackingID) REFERENCES TrackingInfo(trackingID) ON DELETE CASCADE,
    FOREIGN KEY(rentalOrderID) REFERENCES RentalOrder(rentalOrderID) ON DELETE CASCADE
);

CREATE TABLE Worker(
    login CHAR(15) NOT NULL,
    PRIMARY KEY (login),
    FOREIGN KEY(login) REFERENCES Users(login)
);

CREATE TABLE Customer(
    login CHAR(15) NOT NULL,
    PRIMARY KEY (login),
    FOREIGN KEY(login) REFERENCES Users(login)
);
