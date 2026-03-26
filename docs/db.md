# Base de données

## POST `/db/reset`
**Description :** Réinitialise complètement la base de données. Supprime toutes les tables, recrée la structure et réimporte les données initiales (SQL + CSV).

**Authentification :** Token requis / **ADMIN** uniquement

* **Requête :**
  * **Paramètres d'URL / Query :** Aucun
  * **Corps (Body) :** Aucun

* **Réponse attendue :**
    * **Code de succès :** `200 OK`
  * **Exemple de données renvoyées :**
    ```json
    {
      "message": "Base de données réinitialisée avec succès (SQL + CSV)"
    }
    ```

* **Codes d'erreur possibles :**
    * `401 Unauthorized` : `Token absent`
    * `403 Forbidden` : `Accès réservé aux administrateurs`
    * `500 Internal Server Error` : `Erreur SQL ou erreur lors de la lecture du script`
