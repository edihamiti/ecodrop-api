# Points de Collecte

## GET `/points`
**Description :** Liste tous les points de collecte

**Authentification :** Public

* **Requête :**
    * **Paramètres d'URL :**
      * `limit` : La limite, par défaut 10
      * `offset` : L'offset, par défaut 0
    * **Corps (Body) :** Aucun

* **Réponse attendue :**
    * **Code de succès :** `200 OK`
    * **Données renvoyées :** Un tableau de [`CollectionPoint`](/TODO.md)

* **Codes d'erreur possibles :**
  * `204 No Content` : La liste est vide

---

## GET `/points/{id}`
**Description :** Retourne un point de collecte par son ID

**Authentification :** Public

* **Requête :**
  * **Paramètres :** Aucun
  * **Corps (Body) :** Aucun

* **Réponse attendue :**
  * **Code de succès :** `200 OK`
  * **Données renvoyées :** Un [`CollectionPoint`](/TODO.md)

* **Codes d'erreur possibles :**
  * `404 Not Found` : L'id est non inexistant
  * `400 Bad Request` : L'id est non valide

---

## PUT / PATCH `/points/{id}`
**Description :** Modifie (partiellement) un point de collecte par son ID
**Authentification :** Administrateur seulement

* **Requête :**
    * **Types acceptés (Headers) :** `[ex : application/json]`
    * **Corps attendu (Body) :**
      Un objet [`CollectionPoint`](/TODO.md)
      * Exemple :
        ```json
        {
          "adresse": "12 Rue de la Paix",
        }
        ```

* **Réponse attendue :**
    * **Code de succès :** `200 OK`
    * **Exemple de données renvoyées :**
      ```json
      {
        "id": 1,
        "adresse": "12 Rue de la Paix",
        "capaciteMax": 500
      }
      ```

* **Codes d'erreur possibles :**
    * `400 Bad Request` : Corps de la requête invalide ou `id` invalide
    * `401 Unauthorized` : Token manquant ou invalide
    * `403 Forbidden` : Droits insuffisants
    * `404 Not Found` : L'`id` est inexistant

---

## DELETE `/points/{id}/clear`
**Description :** Vide le point de collecte. Le taux d'occupation de ce dépot retombe à 0%
**Authentification :** Administrateur seulement

* **Requête :**
    * **Paramètres d'URL / Query :** `[ex : {id} requis dans l'URL]`
    * **Corps (Body) :** Aucun

* **Réponse attendue :**
    * **Code de succès :** `204 No Content`

* **Codes d'erreur possibles :**
    * `401 Unauthorized` : Token manquant ou invalide
    * `403 Forbidden` : Rôle insuffisant
    * `404 Not Found` : L'ID à supprimer n'existe pas
    * `400 Bad Request` : L'ID à supprimer n'est pas un nombre valide