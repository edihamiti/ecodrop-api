DROP TABLE IF EXISTS CollectionPoint;

CREATE TABLE CollectionPoint(
    id SERIAL,
    adresse TEXT,
    capaciteMax INT);

INSERT INTO CollectionPoint(adresse, capaciteMax) VALUES
('29 rue boileau Armentières', 12);