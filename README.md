# Documentation de l'API Ecodrop

Ce document fournit la documentation de l'API REST pour le projet Ecodrop.

## Liste des administrateurs :

Afin de tester les fonctionnalitées réservées aux administrateurs, il est nécessaire d'utiliser la connection avec Gitlab en utilisant un de ces identifiants :

 - philippe.mathieu@univ-lille.fr
 - jonas.facon.etu@univ-lille.fr
 - edi.hamiti.etu@univ-lille.fr

## Authentification

# JONAS STP REMPLIS CETTE PARTIE

## Endpoints disponibles

[Types de déchets](docs/waste-types.md)

[Points de Collecte](docs/collection-points.md)

## Base de données

# TODO: Ajouter le schéma de la base de données, et les requêtes SQL complexes

```mermaid
classDiagram
direction BT

class users {
   int id PK
   string login
   string password
   string role
}

class wastetype {
   int id PK
   string nom
   int pointsperkilo
}

class collectionpoint {
   int id PK
   string adresse
   int capacitemax
}

class accepts {
   int pointid FK
   int wastetypeid FK
}

class deposit {
   int id PK
   int userid FK
   int pointid FK
   int wastetypeid FK
   int poids
   datetime datedepot
   boolean collected
}

collectionpoint "1" --> "0..*" deposit : pointid
users "1" --> "0..*" deposit : userid
wastetype "1" --> "0..*" deposit : wastetypeid

collectionpoint "1" --> "0..*" accepts : pointid
wastetype "1" --> "0..*" accepts : wastetypeid
```
