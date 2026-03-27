# API Users

| URI | Opération | MIME | Requête | Réponse |
| :--- | :--- | :--- | :--- | :--- |
| /users/leaderboard | GET | <-application/json | | liste des meilleurs utilisateurs (I1) |
| /users/{id} | PUT | ->application/json <-application/json | Utilisateur (I2) | Utilisateur mis à jour (I3) |
| /users/{id} | PATCH | ->application/json <-application/json | Modification (I4) | Utilisateur mis à jour (I3) |

## Corps des requêtes

### I1

Le classement retourne une liste d'utilisateurs avec leurs points cumulés :

```json
{
  "login": "jonas.facon.etu@univ-lille.fr",
  "points": 1250
}
```

### I2

Lors d'une mise à jour complète (PUT) :

```json
{
  "login": "nouveau.login@univ-lille.fr",
  "role": "ADMIN"
}
```

### I3

La représentation complète d'un utilisateur inclut son identifiant :

```json
{
  "id": 1,
  "login": "nouveau.login@univ-lille.fr",
  "role": "ADMIN"
}
```

### I4

Lors d'une mise à jour partielle (PATCH) :

```json
{
  "role": "ADMIN"
}
```

## Exemples

### Consulter le classement des utilisateurs

**GET /users/leaderboard**

Requête vers le serveur :
```
GET /users/leaderboard?limit=2
```

Réponse du serveur :
```json
[
  {
    "login": "jonas.facon.etu@univ-lille.fr",
    "points": 1250
  },
  {
    "login": "alice",
    "points": 850
  }
]
```

Codes de status HTTP :

| Status | Description |
| :--- | :--- |
| 200 OK | La requête s'est effectuée correctement |
| 204 NO CONTENT | Aucun utilisateur trouvé |
| 400 BAD REQUEST | Paramètres de pagination invalides |

### Mettre à jour le rôle d'un utilisateur

**PATCH /users/{id}**

Requête vers le serveur :
```
PATCH /users/1
{
  "role": "ADMIN"
}
```

Réponse du serveur :
```json
{
  "id": 1,
  "login": "ancien.login@univ-lille.fr",
  "role": "ADMIN"
}
```

Codes de status HTTP :

| Status | Description |
| :--- | :--- |
| 200 OK | La requête s'est effectuée correctement |
| 401 UNAUTHORIZED | Token manquant ou invalide |
| 403 FORBIDDEN | Droits insuffisants (Administrateur seulement) |
| 404 NOT FOUND | Utilisateur inexistant |
