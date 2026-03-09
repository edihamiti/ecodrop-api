DROP TABLE IF EXISTS Accepts;
DROP TABLE IF EXISTS WasteType;
DROP TABLE IF EXISTS CollectionPoint;

CREATE TABLE WasteType(
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nom TEXT,
    pointsPerKilo INT);

INSERT INTO WasteType(nom, pointsPerKilo) VALUES
    ('vert', 10),
    ('gravats', 12),
    ('métaux', 30),
    ('métaux', 30),
    ('bois', 20),
    ('plastiques', 9),
    ('textiles', 11),
    ('électronique', 22),
    ('encombrants', 14),
    ('cartons', 7);

CREATE TABLE CollectionPoint(
    id SERIAL PRIMARY KEY ,
    adresse TEXT,
    capaciteMax INT);

INSERT INTO CollectionPoint(adresse, capaciteMax) VALUES
    ('30 rue de la Brasserie Ronchin', 15),
    ('29 rue boileau Armentières', 12);

CREATE TABLE Accepts(
    pointid INT REFERENCES CollectionPoint(id),
    wastetypeid INT REFERENCES WasteType(id));

INSERT INTO Accepts(pointid, wastetypeid) VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4),
    (1, 5),
    (1, 6),
    (1, 7),
    (2, 8),
    (2, 9),
    (2, 10);