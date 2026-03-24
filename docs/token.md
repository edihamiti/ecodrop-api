# Authentification (Token)

## GET `/auth/token`
**Description :** Échange un code d'autorisation OAuth contre un jeton JWT interne à l'application. Cette route est appelée après la redirection du fournisseur OAuth.

**Authentification :** Public (via callback OAuth)

* **Requête :**
  * **Paramètres de Query :**
    * `from` : Le nom du provider (ex: `gitlab`, `google`, `discord`, `github`) - **Requis**
    * `code` : Le code d'autorisation renvoyé par le provider - **Requis**
  * **Corps (Body) :** Aucun

* **Réponse attendue :**
    * **Code de succès :** `200 OK`
  * **Données renvoyées :**
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiJ9..."
    }
    ```

* **Codes d'erreur possibles :**
    * `400 Bad Request` : Paramètres manquants ou provider inconnu.
    * `401 Unauthorized` : L'échange du code a échoué auprès du provider.
    * `500 Internal Server Error` : Erreur lors de la génération du token.
