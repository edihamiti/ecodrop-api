# API Token

| URI | Opération | MIME | Requête | Réponse |
| :--- | :--- | :--- | :--- | :--- |
| /auth/token | GET | <-application/json | | Jeton JWT (I1) |

## Corps des requêtes

### I1

Le jeton JWT est retourné dans un objet JSON :

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

## Exemples

### Échanger un code OAuth contre un jeton

**GET /auth/token**

Requête vers le serveur :
```
GET /auth/token?from=github&code=123456
```

Réponse du serveur :
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Codes de status HTTP :

| Status | Description |
| :--- | :--- |
| 200 OK | La requête s'est effectuée correctement |
| 400 BAD REQUEST | Paramètres manquants ou provider inconnu |
| 401 UNAUTHORIZED | L'échange du code a échoué auprès du provider |
| 500 INTERNAL SERVER ERROR | Erreur lors de la génération du token |
