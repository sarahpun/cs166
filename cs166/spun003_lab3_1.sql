DROP TABLE IF EXISTS Professor CASCADE;
DROP TABLE IF EXISTS Dept CASCADE;
DROP TABLE IF EXISTS Project_Manage CASCADE;
DROP TABLE IF EXISTS Graduate_Major CASCADE;
DROP TABLE IF EXISTS work_in CASCADE;
DROP TABLE IF EXISTS work_dept CASCADE;
DROP TABLE IF EXISTS work_proj_supervise CASCADE;
DROP TABLE IF EXISTS advise CASCADE;
DROP TABLE IF EXISTS runs CASCADE;

CREATE TABLE Professor (
    ssn CHAR(11) NOT NULL, 
    name CHAR(32) NOT NULL, 
    age INTEGER, 
    rank CHAR(32) NOT NULL, 
    specialty CHAR(32) NOT NULL, 
    PRIMARY KEY (ssn)
);


CREATE TABLE Dept(
    dno INTEGER, 
    ssn CHAR(11) NOT NULL,
    dname CHAR(32) NOT NULL,
    office CHAR(32) NOT NULL, 
    PRIMARY KEY(dno),
    FOREIGN KEY(ssn) REFERENCES Professor(ssn) ON DELETE NO ACTION
);

CREATE TABLE Project_Manage (
    pno INTEGER, 
    ssn CHAR(11) NOT NULL,
    sponsor CHAR(32) NOT NULL, 
    start_date CHAR(8) NOT NULL, 
    end_date CHAR(8) NOT NULL, 
    bugdet INTEGER, 
    PRIMARY KEY(pno),
    FOREIGN KEY(ssn) REFERENCES Professor(ssn) ON DELETE NO ACTION
);

CREATE TABLE Graduate_Major (
    dno INTEGER, 
    ssn CHAR(11), 
    name CHAR(32) NOT NULL, 
    age INTEGER, 
    deg_pg CHAR(32) NOT NULL, 
    PRIMARY KEY(ssn),
    FOREIGN KEY(dno) REFERENCES Dept(dno)
    
    );

CREATE TABLE work_in(
    ssn CHAR(11) NOT NULL,
    pno INTEGER,
    PRIMARY KEY(ssn,pno),
    FOREIGN KEY(ssn) REFERENCES Professor(ssn),
    FOREIGN KEY(pno) REFERENCES Project(pno)
   );

CREATE TABLE runs(
    ssn CHAR(11) NOT NULL,
    dno INTEGER,
    Primary Key(ssn, dno),
    FOREIGN KEY(ssn) REFERENCES Professor(ssn),
    FOREIGN KEY(dno) REFERENCES Dept(dno)
);

CREATE TABLE work_dept(
    ssn CHAR(11) NOT NULL,
    dno INTEGER,
    time_pc INTEGER,
    Primary Key(ssn, dno),
    FOREIGN KEY(ssn) REFERENCES Professor(ssn),
    FOREIGN KEY(dno) REFERENCES Dept(dno)
);

CREATE TABLE work_proj_supervise(
    since CHAR(10) NOT NULL,
    pno INTEGER,
    ssn CHAR(11) NOT NULL,
    ssn1 CHAR(11) NOT NULL,
    PRIMARY KEY(pno, ssn, ssn1),
    FOREIGN KEY(ssn1) REFERENCES Professor(ssn),
    FOREIGN KEY(pno) REFERENCES Project(pno),
    FOREIGN KEY(ssn) REFERENCES Graduate(ssn)
);

CREATE TABLE advise(
    ssn CHAR(11) NOT NULL,
	ssn1 CHAR(11) NOT NULL,
    PRIMARY KEY(ssn, ssn1),
	FOREIGN KEY(ssn) REFERENCES Graduate(ssn),
	FOREIGN KEY(ssn1) REFERENCES Graduate(ssn)
);


	



