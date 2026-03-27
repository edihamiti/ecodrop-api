# API Deposits

| URI | Opération | MIME | Requête | Réponse |
| :--- | :--- | :--- | :--- | :--- |
| /deposits | GET | <-application/json | | liste des dépôts (I1) |
| /deposits/{id} | GET | <-application/json | | un dépôt (I1) ou 404 |
| /deposits | POST | ->application/json <-application/json | Dépôt (I2) | Nouveau dépôt (I1) |
| /deposits/{id} | PATCH | ->application/json <-application/json | Modification (I3) | Dépôt mis à jour (I1) |

## Corps des requêtes

### I1

Un dépôt comporte un identifiant, l'ID de l'utilisateur, le point de collecte, le type de déchet, le poids, la date et le statut de collecte. Sa représentation JSON (I1) est la suivante :

```json
{
  "id": 1,
  "userId": 5,
  "point": {
    "id": 1,
    "adresse": "12 Rue de la Paix",
    "capaciteMax": 500
  },
  "wasteType": {
    "id": 1,
    "nom": "plastiques",
    "pointsPerKilo": 10
  },
  "weight": 10.5,
  "dateDepot": "2024-03-24T10:00:00Z",
  "collected": false
}
```

### I2

Lors de la création d'un dépôt, on spécifie le point, le type de déchet et le poids :

```json
{
  "point": { "id": 1 },
  "wasteType": { "id": 2 },
  "weight": 10.5
}
```

### I3

Pour marquer un dépôt comme collecté :

```json
{
  "collected": true
}
```

## Exemples

### Lister tous les dépôts effectués

**GET /deposits**

Requête vers le serveur :
```
GET /deposits?limit=1
```

Réponse du serveur :
```json
[
  {
    "id": 1,
    "userId": 5,
    "point": { "id": 1, "adresse": "12 Rue de la Paix", "capaciteMax": 500 },
    "wasteType": { "id": 1, "nom": "plastiques", "pointsPerKilo": 10 },
    "weight": 10.5,
    "dateDepot": "2024-03-24T10:00:00Z",
    "collected": false
  }
]
```

Codes de status HTTP :

| Status | Description |
| :--- | :--- |
| 200 OK | La requête s'est effectuée correctement |
| 401 UNAUTHORIZED | Token manquant ou invalide |
| 403 FORBIDDEN | Droits insuffisants (Administrateur seulement) |

### Enregistrer un nouveau dépôt

**POST /deposits**

Requête vers le serveur :
```
POST /deposits
{
  "point": { "id": 1 },
  "wasteType": { "id": 1 },
  "weight": 5.0
}
```

Réponse du serveur :
```json
{
  "id": 2,
  "userId": 5,
  "point": { "id": 1, "adresse": "12 Rue de la Paix", "capaciteMax": 500 },
  "wasteType": { "id": 1, "nom": "plastiques", "pointsPerKilo": 10 },
  "weight": 5.0,
  "dateDepot": "2024-03-27T14:00:00Z",
  "collected": false
}
```

Codes de status HTTP :

| Status | Description |
| :--- | :--- |
| 201 CREATED | Le dépôt a été enregistré |
| 400 BAD REQUEST | Données manquantes ou poids négatif |
| 403 FORBIDDEN | Le point de collecte est saturé |

### Marquer un dépôt comme collecté

**PATCH /deposits/{id}**

Requête vers le serveur :
```
PATCH /deposits/1
{
  "collected": true
}
```

Réponse du serveur :
```json
{
  "id": 1,
  "userId": 5,
  "point": { "id": 1, "adresse": "12 Rue de la Paix", "capaciteMax": 500 },
  "wasteType": { "id": 1, "nom": "plastiques", "pointsPerKilo": 10 },
  "weight": 10.5,
  "dateDepot": "2024-03-24T10:00:00Z",
  "collected": true
}
```

Codes de status HTTP :

| Status | Description |
| :--- | :--- |
| 200 OK | La requête s'est effectuée correctement |
| 401 UNAUTHORIZED | Token manquant ou invalide |
| 403 FORBIDDEN | Droits insuffisants (Administrateur seulement) |
| 404 NOT FOUND | Le dépôt n'existe pas |
