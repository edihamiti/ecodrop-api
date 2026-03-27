# API WasteTypes

| URI | Opération | MIME | Requête | Réponse |
| :--- | :--- | :--- | :--- | :--- |
| /waste-types | GET | <-application/json | | liste des types de déchets (I1) |
| /waste-types/{id} | GET | <-application/json | | un type de déchet (I1) ou 404 |
| /waste-types | POST | ->application/json <-application/json | Type de déchet (I2) | Nouveau type de déchet (I1) |
| /waste-types/{id} | DELETE | | | 204 ou 404 ou 409 |

## Corps des requêtes

### I1

Un type de déchet comporte un identifiant, un nom et un nombre de points par kilo. Sa représentation JSON (I1) est la suivante :

```json
{
  "id": 1,
  "nom": "plastiques",
  "pointsPerKilo": 10
}
```

### I2

Lors de la création, l'identifiant n'est pas connu car il sera fourni par le serveur. Aussi on aura une représentation JSON (I2) qui comporte uniquement le nom et les points par kilo :

```json
{
  "nom": "verre",
  "pointsPerKilo": 5
}
```

## Exemples

### Lister tous les types de déchets connus dans la base de données

**GET /waste-types**

Requête vers le serveur :
```
GET /waste-types
```

Réponse du serveur :
```json
[
  {
    "id": 1,
    "nom": "plastiques",
    "pointsPerKilo": 10
  },
  {
    "id": 2,
    "nom": "verre",
    "pointsPerKilo": 5
  }
]
```

Codes de status HTTP :

| Status | Description |
| :--- | :--- |
| 200 OK | La requête s'est effectuée correctement |
| 204 No Content | La liste est vide |

### Récupérer les détails d'un type de déchet

**GET /waste-types/{id}**

Requête vers le serveur :
```
GET /waste-types/1
```

Réponse du serveur :
```json
{
  "id": 1,
  "nom": "plastiques",
  "pointsPerKilo": 10
}
```

Codes de status HTTP :

| Status | Description |
| :--- | :--- |
| 200 OK | La requête s'est effectuée correctement |
| 404 NOT FOUND | Le type de déchet n'existe pas |
| 400 BAD REQUEST | L'id est non valide |

### Ajouter un type de déchet

**POST /waste-types**

Requête vers le serveur :
```
POST /waste-types
{
  "nom": "métaux",
  "pointsPerKilo": 15
}
```

Réponse du serveur :
```json
{
  "id": 3,
  "nom": "métaux",
  "pointsPerKilo": 15
}
```

Codes de status HTTP :

| Status | Description |
| :--- | :--- |
| 201 CREATED | Le type de déchet a été ajouté |
| 400 BAD REQUEST | Données invalides |
| 401 UNAUTHORIZED | Token manquant ou invalide |
| 403 FORBIDDEN | Droits insuffisants (Administrateur seulement) |

### Supprimer un type de déchet

**DELETE /waste-types/{id}**

Requête vers le serveur :
```
DELETE /waste-types/3
```

Codes de status HTTP :

| Status | Description |
| :--- | :--- |
| 204 NO CONTENT | Le type de déchet a été supprimé |
| 404 NOT FOUND | Le type de déchet n'existe pas |
| 409 CONFLICT | Impossible de supprimer car type utilisé dans des dépôts |
| 401 UNAUTHORIZED | Token manquant ou invalide |
| 403 FORBIDDEN | Droits insuffisants (Administrateur seulement) |
