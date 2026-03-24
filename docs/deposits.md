# Dépôts

## GET `/deposits`
**Description :** Récupère la liste de tous les dépôts effectués.

**Authentification :** Administrateur seulement (D'après `SecurityFilter`)

* **Requête :**
  * **Paramètres de Query :**
    * `limit` : Nombre maximum de dépôts, par défaut 10.
    * `offset` : Décalage pour la pagination, par défaut 0.
  * **Corps (Body) :** Aucun

* **Réponse attendue :**
    * **Code de succès :** `200 OK`
  * **Données renvoyées :** Liste de [`Deposit`](/docs/types.md#deposit)
    ```json
    [
      {
        "id": 1,
        "userId": 5,
        "point": { "id": 1, "adresse": "...", "capaciteMax": 150 },
        "wasteType": { "id": 1, "nom": "vert", "pointsPerKilo": 10 },
        "weight": 5.0,
        "dateDepot": "2024-03-24T10:00:00Z",
        "collected": false
      }
    ]
    ```

---

## GET `/deposits/{id}`
**Description :** Récupère les détails d'un dépôt spécifique par son ID.

**Authentification :** Administrateur seulement

* **Requête :**
    * **Paramètres d'URL :** `id` (Integer) requis.

* **Réponse attendue :**
    * **Code de succès :** `200 OK`
    * **Données renvoyées :** Un objet `Deposit`.

---

## POST `/deposits`
**Description :** Enregistre un nouveau dépôt de déchets.

**Authentification :** Utilisateur ou Administrateur

* **Requête :**
    * **Corps attendu (Body) :**
      ```json
      {
        "point": { "id": 1 },
        "wasteType": { "id": 2 },
        "weight": 10.5
      }
      ```

* **Réponse attendue :**
    * **Code de succès :** `201 Created`
    * **Exemple de données renvoyées :** L'objet `Deposit` créé avec son ID et la date.

* **Codes d'erreur possibles :**
    * `400 Bad Request` : Données manquantes ou poids négatif.
    * `403 Forbidden` : Le point de collecte est saturé (Capacité max atteinte).

---

## PUT / PATCH `/deposits/{id}`
**Description :** Mise à jour d'un dépôt (ex: marquer comme collecté).

**Authentification :** Administrateur seulement

* **Requête :**
    * **Corps attendu (PATCH) :**
      ```json
      {
        "collected": true
      }
      ```

* **Réponse attendue :**
    * **Code de succès :** `200 OK`
