# Utilisateurs

## GET `/users/leaderboard`
**Description :** Récupère le classement des utilisateurs par points (basé sur leurs dépôts).

**Authentification :** Public

* **Requête :**
  * **Paramètres de Query :**
    * `limit` : Nombre maximum d'utilisateurs, par défaut 10.
    * `offset` : Décalage pour la pagination, par défaut 0.
  * **Corps (Body) :** Aucun

* **Réponse attendue :**
    * **Code de succès :** `200 OK`
  * **Données renvoyées :** Liste de [`LeaderBoardUser`](/docs/types.md#leaderboarduser)
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

* **Codes d'erreur possibles :**
    * `204 No Content` : Aucun utilisateur trouvé.
    * `400 Bad Request` : Paramètres de pagination invalides.

---

## PUT `/users/{id}`
**Description :** Met à jour complètement un utilisateur.

**Authentification :** Administrateur seulement

* **Requête :**
    * **Paramètres d'URL :** `id` (Integer) requis.
    * **Corps attendu (Body) :**
      ```json
      {
        "login": "nouveau.login@univ-lille.fr",
        "role": "ADMIN"
      }
      ```

* **Réponse attendue :**
    * **Code de succès :** `200 OK`
    * **Exemple de données renvoyées :**
      ```json
      {
        "id": 1,
        "login": "nouveau.login@univ-lille.fr",
        "role": "ADMIN"
      }
      ```

* **Codes d'erreur possibles :**
    * `400 Bad Request` : Corps invalide ou champs manquants.
    * `401 Unauthorized` : Token manquant ou invalide.
    * `403 Forbidden` : Droits insuffisants.
    * `404 Not Found` : Utilisateur non trouvé.

---

## PATCH `/users/{id}`
**Description :** Met à jour partiellement un utilisateur (ex: changer uniquement le rôle).

**Authentification :** Administrateur seulement

* **Requête :**
    * **Paramètres d'URL :** `id` (Integer) requis.
    * **Corps attendu (Body) :**
      ```json
      {
        "role": "ADMIN"
      }
      ```

* **Réponse attendue :**
    * **Code de succès :** `200 OK`
    * **Exemple de données renvoyées :**
      ```json
      {
        "id": 1,
        "login": "ancien.login@univ-lille.fr",
        "role": "ADMIN"
      }
      ```

* **Codes d'erreur possibles :**
    * `401 Unauthorized` : Token manquant.
    * `404 Not Found` : Utilisateur inexistant.
