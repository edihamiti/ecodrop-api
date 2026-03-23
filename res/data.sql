DROP TABLE IF EXISTS Accepts;
DROP TABLE IF EXISTS Deposit;
DROP TABLE IF EXISTS CollectionPoint;;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS WasteType;

CREATE TABLE WasteType
(
    id            INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nom           TEXT,
    pointsPerKilo INT
);

INSERT INTO WasteType(nom, pointsPerKilo)
VALUES ('vert', 10),
       ('gravats', 12),
       ('métaux', 30),
       ('bois', 20),
       ('plastiques', 9),
       ('textiles', 11),
       ('électronique', 22),
       ('encombrants', 14),
       ('cartons', 7),
       ('papier', 8),
       ('verre', 16),
       ('piles', 40),
       ('ampoules', 25),
       ('déchets alimentaires', 6),
       ('huiles usagées', 18),
       ('peintures', 24),
       ('solvants', 28),
       ('batteries', 35),
       ('ferraille', 19),
       ('meubles', 13),
       ('déchets verts', 9),
       ('radiographies', 21),
       ('médicaments périmés', 27);

CREATE TABLE CollectionPoint
(
    id          INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    adresse     TEXT,
    capaciteMax INT
);

INSERT INTO CollectionPoint(adresse, capaciteMax)
VALUES ('30 rue de la Brasserie Ronchin', 150),
       ('29 rue boileau Armentières', 12),
       ('28 rue de la Brasserie Ronchin', 10),
       ('27 rue de la Brasserie Ronchin', 8),
       ('26 rue de la Brasserie Ronchin', 6),
       ('25 rue de la Brasserie Ronchin', 4),
       ('24 rue de la Brasserie Ronchin', 2),
       ('23 rue de la Brasserie Ronchin', 1),
       ('22 rue de la Brasserie Ronchin', 0),
       ('21 rue de la Brasserie Ronchin', 0),
       ('14 rue le nôtre Wattignies', 15),
       ('13 rue le nôtre Wattignies', 12),
       ('12 rue le nôtre Wattignies', 10),
       ('11 rue le nôtre Wattignies', 8),
       ('10 rue le nôtre Wattignies', 6),
       ('9 rue le nôtre Wattignies', 4),
       ('8 rue le nôtre Wattignies', 2),
       ('7 rue le nôtre Wattignies', 1),
       ('6 rue le nôtre Wattignies', 0),
       ('5 rue le nôtre Wattignies', 0);

CREATE TABLE Accepts
(
    pointid     INT REFERENCES CollectionPoint (id),
    wastetypeid INT REFERENCES WasteType (id)
);

INSERT INTO Accepts(pointid, wastetypeid)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (2, 8),
       (2, 9),
       (2, 10),
       (3, 1),
       (3, 2),
       (3, 3),
       (3, 4),
       (3, 5),
       (3, 6),
       (3, 7),
       (4, 8),
       (4, 9),
       (4, 10),
       (5, 1),
       (5, 2),
       (5, 3),
       (5, 4),
       (5, 5),
       (5, 6),
       (5, 7),
       (6, 8),
       (6, 9),
       (6, 10),
       (7, 1),
       (7, 2),
       (7, 3),
       (7, 4),
       (7, 5),
       (7, 6),
       (7, 7),
       (8, 8),
       (8, 9),
       (8, 10),
       (9, 1),
       (9, 2),
       (9, 3),
       (9, 4),
       (9, 5),
       (9, 6),
       (9, 7),
       (10, 8),
       (10, 9),
       (10, 10),
       (11, 1),
       (11, 2),
       (11, 3),
       (11, 4),
       (11, 5),
       (11, 6),
       (11, 7),
       (12, 8),
       (12, 9),
       (12, 10),
       (13, 1),
       (13, 2),
       (13, 3),
       (13, 4),
       (13, 5),
       (13, 6),
       (13, 7),
       (14, 8),
       (14, 9),
       (14, 10),
       (15, 1),
       (15, 2),
       (15, 3),
       (15, 4),
       (15, 5),
       (15, 6),
       (15, 7),
       (16, 8),
       (16, 9),
       (16, 10);

CREATE TABLE Users
(
    id       INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    login    TEXT UNIQUE,
    role     TEXT NOT NULL DEFAULT 'USER'
);

INSERT INTO Users(login, role)
VALUES ('enzo', 'USER'),
       ('alice', 'USER'),
       ('bob', 'USER'),
       ('charlie', 'USER'),
       ('dave', 'USER'),
       ('eve', 'USER'),
       ('frank', 'USER'),
       ('grace', 'USER'),
       ('heidi', 'USER'),
       ('ivan', 'USER'),
       ('ADMIN', 'ADMIN'),
       ('jonas', 'ADMIN'),
       ('lucas', 'ADMIN'),
       ('maria', 'ADMIN'),
       ('edi', 'ADMIN'),
       ('nina', 'ADMIN'),
       ('leo', 'ADMIN'),
       ('emma', 'ADMIN'),
       ('oliver', 'ADMIN'),
       ('ava', 'ADMIN'),
        ('jonas.facon.etu@univ-lille.fr', 'ADMIN'),
       ('edi.hamiti.etu@univ-lille.fr', 'ADMIN'),
       ('philippe.mathieu@univ-lille.fr', 'ADMIN');


CREATE TABLE Deposit
(
    id          INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    userid      INT REFERENCES Users (id),
    pointid     INT REFERENCES CollectionPoint (id),
    wasteTypeId INT REFERENCES WasteType (id),
    poids       INT,
    dateDepot   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    collected   BOOLEAN DEFAULT FALSE
);

INSERT INTO Deposit(userid, pointid, wasteTypeId, poids)
VALUES (1, 1, 1, 5),
       (1, 1, 2, 10),
       (2, 2, 8, 3),
       (2, 3, 1, 7),
       (3, 1, 4, 12),
       (3, 5, 3, 8),
       (4, 4, 9, 4),
       (4, 6, 10, 6),
       (5, 2, 8, 9),
       (5, 7, 5, 2),
       (6, 3, 2, 11),
       (6, 9, 6, 5),
       (7, 11, 1, 14),
       (7, 12, 7, 3),
       (8, 13, 3, 8),
       (8, 14, 4, 6),
       (9, 15, 5, 10),
       (9, 16, 9, 7),
       (10, 11, 2, 4),
       (10, 13, 6, 9),
       (11, 12, 10, 12),
       (11, 14, 7, 5),
       (12, 15, 8, 11),
       (12, 16, 10, 10),
       (13, 11, 9, 14),
       (13, 13, 1, 15),
       (14, 12, 3, 12),
       (14, 16, 4, 15),
       (15, 13, 5, 13);
