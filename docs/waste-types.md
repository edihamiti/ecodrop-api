# Types de déchets

## GET `/waste-types`
**Description :** Liste tous les types de déchets disponibles.

**Authentification :** Public

* **Requête :**
    * **Paramètres :**
      * `limit` : La limite, optionnel par défaut 10
      * `offset` : L'offset, optionnel par défaut 0
    * **Corps (Body) :** Aucun

* **Réponse attendue :**
    * **Code de succès :** `200 OK`
    * **Données renvoyées :** Un tableau de [`WasteType`](/TODO.md)

* **Codes d'erreur possibles :**
    * `204 No Content` : La liste est vide

---

## GET `/waste-types/{id}`
**Description :** Retourne un type de déchet par son ID.

**Authentification :** Public

* **Requête :**
  * **Paramètres :** Aucun
  * **Corps (Body) :** Aucun

* **Réponse attendue :**
  * **Code de succès :** `200 OK`
  * **Données renvoyées :** Un [`WasteType`](/TODO.md)

* **Codes d'erreur possibles :**
  * `404 Not Found` : L'id est non inexistant
  * `400 Bad Request` : L'id est non valide

---

## POST `/waste-types`
**Description :** Ajoute un nouveau type

**Authentification :** Administrateur seulement

* **Requête :**
    * **Corps attendu (Body) :** Un objet [`WasteType`](/TODO.md)

* **Réponse attendue :**
    * **Code de succès :** `201 Created`
    * **Exemple de données renvoyées :**
      ```json
      {
        "id": 33,
        "nom": "test",
        "pointsPerKilo": 5
      }
      ```

* **Codes d'erreur possibles :**
    * `400 Bad Request` : Données invalides
    * `401 Unauthorized` : Token manquant ou invalide
    * `403 Forbidden` : Droits insuffisants

---

## DELETE `/waste-types/{id}`
**Description :** Supprime un type

**Authentification :** Administrateur seulement

* **Requête :**
    * **Corps (Body) :** Aucun

* **Réponse attendue :**
    * **Code de succès :** `204 No Content`
    * **Exemple de données renvoyées :** Aucun

* **Codes d'erreur possibles :**
    * `409 Conflict` : Impossible de supprimer car type utilisé dans le table des dépots
    * `404 Not Found` : L'id est non inexistant
 