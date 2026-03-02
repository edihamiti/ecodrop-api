DROP TABLE IF EXISTS WasteType;

CREATE TABLE WasteType(
                          id SERIAL,
                          nom TEXT,
                          pointsPerKilo INT
);

INSERT INTO WasteType(nom, pointsPerKilo)
VALUES ('vert', 10);

INSERT INTO WasteType(nom, pointsPerKilo)
VALUES ('gravats', 12);

INSERT INTO WasteType(nom, pointsPerKilo)
VALUES ('métaux', 30);

INSERT INTO WasteType(nom, pointsPerKilo)
VALUES ('métaux', 30);

INSERT INTO WasteType(nom, pointsPerKilo)
VALUES ('bois', 20);

INSERT INTO WasteType(nom, pointsPerKilo)
VALUES ('plastiques', 9);

INSERT INTO WasteType(nom, pointsPerKilo)
VALUES ('textiles', 11);

INSERT INTO WasteType(nom, pointsPerKilo)
VALUES ('électronique', 22);

INSERT INTO WasteType(nom, pointsPerKilo)
VALUES ('encombrants', 14);

INSERT INTO WasteType(nom, pointsPerKilo)
VALUES ('cartons', 7);


DROP TABLE IF EXISTS CollectionPoint;

CREATE TABLE CollectionPoint(
    id SERIAL,
    adresse TEXT,
    capaciteMax INT);

INSERT INTO CollectionPoint(adresse, capaciteMax) VALUES
('29 rue boileau Armentières', 12);