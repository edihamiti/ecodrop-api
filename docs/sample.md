# [Nom de la ressource, ex : Types de déchets]

## GET `[Chemin de l'endpoint, ex : /waste-types]`
**Description :** [Que fait cette méthode ?]

**Authentification :** [Public / Token requis / Rôle spécifique]

* **Requête :**
  * **Paramètres d'URL / Query :**
    * `limit` : La limite, par défaut 10
    * `offset` : L'offset, par défaut 0
  * **Corps (Body) :** Aucun

* **Réponse attendue :**
    * **Code de succès :** `[ex : 200 OK]`
  * **Données renvoyées :** Exemple de [`retour`](/TODO.md)

* **Codes d'erreur possibles :**
    * `[ex : 404 Not Found]` : `[Raison, ex : Ressource introuvable]`
    * `[ex : 500 Internal Server Error]` : `[Raison, ex : Erreur base de données]`

---

## POST `[Chemin de l'endpoint, ex : /waste-types]`
**Description :** [Que fait cette méthode ?]

**Authentification :** [Public / Token requis / Rôle spécifique]

* **Requête :**
    * **Corps attendu (Body) :**
      ```json
      {
        "champ1": "type (ex: String, requis)",
        "champ2": "type (ex: Integer, min: 0)"
      }
      ```

* **Réponse attendue :**
    * **Code de succès :** `[ex : 201 Created]`
    * **Exemple de données renvoyées :**
      ```json
      [Insérer l'exemple JSON de l'objet créé ici]
      ```

* **Codes d'erreur possibles :**
    * `[ex : 400 Bad Request]` : `[Raison, ex : Données invalides ou manquantes]`
    * `[ex : 401 Unauthorized]` : `[Raison, ex : Token manquant ou invalide]`
    * `[ex : 403 Forbidden]` : `[Raison, ex : Droits insuffisants]`
    * `[ex : 409 Conflict]` : `[Raison, ex : La ressource existe déjà]`

---

## PUT / PATCH `[Chemin de l'endpoint, ex : /waste-types]`
**Description :** [Que fait cette méthode ? Mise à jour complète ou partielle ?]

**Authentification :** [Public / Token requis / Rôle spécifique]

* **Requête :**
    * **Paramètres d'URL / Query :** `[ex : {id} requis dans l'URL]`
    * **Corps attendu (Body) :**
      ```json
      {
        "champ_a_modifier": "nouvelle valeur"
      }
      ```

* **Réponse attendue :**
    * **Code de succès :** `[ex : 200 OK ou 204 No Content]`
    * **Exemple de données renvoyées :** `[JSON de l'objet mis à jour, ou vide si 204]`

* **Codes d'erreur possibles :**
    * `[ex : 400 Bad Request]` : `[Raison, ex : Données de mise à jour invalides]`
    * `[ex : 401 Unauthorized]` : `[Raison, ex : Token manquant]`
    * `[ex : 404 Not Found]` : `[Raison, ex : L'ID à modifier n'existe pas]`

---

## DELETE `[Chemin de l'endpoint, ex : /waste-types]`
**Description :** [Que fait cette méthode ?]

**Authentification :** [Public / Token requis / Rôle spécifique]

* **Requête :**
    * **Paramètres d'URL / Query :** `[ex : {id} requis dans l'URL]`
    * **Corps (Body) :** Aucun

* **Réponse attendue :**
    * **Types renvoyés (Headers) :** Aucun
    * **Exemple de données renvoyées :** Aucun

* **Codes d'erreur possibles :**
    * `[ex : 401 Unauthorized]` : `[Raison, ex : Token manquant]`
    * `[ex : 403 Forbidden]` : `[Raison, ex : Rôle insuffisant]`
    * `[ex : 404 Not Found]` : `[Raison, ex : L'ID à supprimer n'existe pas]`
    * `[ex : 409 Conflict]` : `[Raison, ex : Impossible de supprimer car lié à d'autres données]`