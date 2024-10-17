DROP TABLE IF EXISTS Musicians CASCADE;
DROP TABLE IF EXISTS Album CASCADE;
DROP TABLE IF EXISTS Instrument CASCADE;
DROP TABLE IF EXISTS Song CASCADE;
DROP TABLE IF EXISTS Place CASCADE;
DROP TABLE IF EXISTS Producer CASCADE;
DROP TABLE IF EXISTS Plays CASCADE;
DROP TABLE IF EXISTS Perform CASCADE;
DROP TABLE IF EXISTS Live CASCADE;
DROP TABLE IF EXISTS Appears CASCADE;
DROP TABLE IF EXISTS Home_Telephone CASCADE;

CREATE TABLE Musicians(
    ssn CHAR(11) NOT NULL,
    name CHAR(32) NOT NULL,
    PRIMARY KEY(ssn)
);

CREATE TABLE Album(
    albumid INTEGER,
    copyrightDate CHAR(8) NOT NULL,
    speed INTEGER,
    title CHAR(32) NOT NULL,
    ssn CHAR(11) NOT NULL,
    PRIMARY KEY(albumid),
    FOREIGN KEY(ssn) REFERENCES Musicians(ssn)

);

CREATE TABLE Instrument(
    inst_id INTEGER,
    dname CHAR(32) NOT NULL,
    keys CHAR(10) NOT NULL,
    PRIMARY KEY(inst_id)

);

CREATE TABLE Song(
    songid INTEGER,
    title CHAR(32) NOT NULL,
    author CHAR (32) NOT NULL,
    albumid INTEGER,
    PRIMARY KEY(songid),
    FOREIGN KEY(albumid) REFERENCES Album(albumid)
);

CREATE TABLE Place(
    address_ CHAR(32) NOT NULL,
    PRIMARY KEY(address_)
);

CREATE TABLE Producer(
    ssn CHAR(11) NOT NULL,
    albumid INTEGER,
    PRIMARY KEY(ssn, albumid),
    FOREIGN KEY(ssn) REFERENCES Musicians(ssn),
    FOREIGN KEY(albumid) REFERENCES Album(albumid)
);

CREATE TABLE Plays(
    ssn CHAR(11) NOT NULL,
    instr_id INTEGER,
    PRIMARY KEY(ssn, instr_id),
    FOREIGN KEY(ssn) REFERENCES Musicians(ssn),
    FOREIGN KEY(instr_id) REFERENCES Instrument(inst_id) 
);

CREATE TABLE Perform(
    ssn CHAR(11) NOT NULL,
    songid INTEGER,
    PRIMARY KEY(ssn, songid),
    FOREIGN KEY(ssn) REFERENCES Musicians(ssn),
    FOREIGN KEY(songid) REFERENCES Song(songid)
);

CREATE TABLE Appears(
    albumid INTEGER,
    songid INTEGER,
    PRIMARY KEY(albumid, songid),
    FOREIGN KEY(albumid) REFERENCES Album(albumid),
    FOREIGN KEY(songid) REFERENCES Song(songid)
);

CREATE TABLE Home_Telephone(
    ssn CHAR(11) NOT NULL,
    address_ CHAR(32) NOT NULL,
    phone_no CHAR(13) NOT NULL,
    PRIMARY KEY(ssn, address_, phone_no),
    FOREIGN KEY(ssn) REFERENCES Musicians(ssn),
    FOREIGN KEY(address_) REFERENCES Place(address_)
);

CREATE TABLE Live(
    ssn CHAR(11) NOT NULL,
    address_ CHAR(32) NOT NULL,
    PRIMARY KEY(ssn, address_),
    FOREIGN KEY(ssn) REFERENCES Musicians(ssn),
    FOREIGN KEY(address_) REFERENCES Place(address_)
);







