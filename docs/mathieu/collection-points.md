# API CollectionPoints

| URI | Opération | MIME | Requête | Réponse |
| :--- | :--- | :--- | :--- | :--- |
| /points | GET | <-application/json | | liste des points de collecte (I1) |
| /points/{id} | GET | <-application/json | | un point de collecte avec types (I2) ou 404 |
| /points/{id} | PUT/PATCH | ->application/json <-application/json | Point de collecte (I3) | Point de collecte mis à jour (I1) |
| /points/{id}/clear | DELETE | | | 204 ou 404 |

## Corps des requêtes

### I1

Un point de collecte comporte un identifiant, une adresse et une capacité maximale. Sa représentation JSON (I1) est la suivante :

```json
{
  "id": 1,
  "adresse": "12 Rue de la Paix",
  "capaciteMax": 500
}
```

### I2

Lors de la consultation détaillée, le point de collecte inclut les types de déchets acceptés :

```json
{
  "id": 1,
  "adresse": "12 Rue de la Paix",
  "capaciteMax": 500,
  "acceptedWasteTypes": [
    {
      "id": 1,
      "nom": "plastiques",
      "pointsPerKilo": 10
    }
  ]
}
```

### I3

Pour la mise à jour, on peut envoyer un objet partiel ou complet :

```json
{
  "adresse": "15 Rue de la Paix",
  "capaciteMax": 600
}
```

## Exemples

### Lister tous les points de collecte

**GET /points**

Requête vers le serveur :
```
GET /points?limit=2&offset=0
```

Réponse du serveur :
```json
[
  {
    "id": 1,
    "adresse": "12 Rue de la Paix",
    "capaciteMax": 500
  },
  {
    "id": 2,
    "adresse": "5 Avenue des Champs",
    "capaciteMax": 300
  }
]
```

Codes de status HTTP :

| Status | Description |
| :--- | :--- |
| 200 OK | La requête s'est effectuée correctement |
| 204 No Content | La liste est vide |

### Récupérer les détails d'un point de collecte

**GET /points/{id}**

Requête vers le serveur :
```
GET /points/1
```

Réponse du serveur :
```json
{
  "id": 1,
  "adresse": "12 Rue de la Paix",
  "capaciteMax": 500,
  "acceptedWasteTypes": [
    {
      "id": 1,
      "nom": "plastiques",
      "pointsPerKilo": 10
    }
  ]
}
```

Codes de status HTTP :

| Status | Description |
| :--- | :--- |
| 200 OK | La requête s'est effectuée correctement |
| 404 NOT FOUND | L'id est inexistant |
| 400 BAD REQUEST | L'id est non valide |

### Modifier un point de collecte

**PATCH /points/{id}**

Requête vers le serveur :
```
PATCH /points/1
{
  "adresse": "15 Rue de la Paix"
}
```

Réponse du serveur :
```json
{
  "id": 1,
  "adresse": "15 Rue de la Paix",
  "capaciteMax": 500
}
```

Codes de status HTTP :

| Status | Description |
| :--- | :--- |
| 200 OK | La requête s'est effectuée correctement |
| 400 BAD REQUEST | Corps de la requête invalide ou id invalide |
| 401 UNAUTHORIZED | Token manquant ou invalide |
| 403 FORBIDDEN | Droits insuffisants (Administrateur seulement) |
| 404 NOT FOUND | L'id est inexistant |

### Vider un point de collecte

**DELETE /points/{id}/clear**

Requête vers le serveur :
```
DELETE /points/1/clear
```

Codes de status HTTP :

| Status | Description |
| :--- | :--- |
| 204 NO CONTENT | Le point de collecte a été vidé |
| 404 NOT FOUND | L'id est inexistant |
| 400 BAD REQUEST | L'id n'est pas un nombre valide |
| 401 UNAUTHORIZED | Token manquant ou invalide |
| 403 FORBIDDEN | Droits insuffisants (Administrateur seulement) |
