# Documentation de l'API Ecodrop

Ce document fournit la documentation de l'API REST pour le projet Ecodrop.

## URL de Base

L'API est servie à partir de la racine de l'application web. Tous les points de terminaison sont relatifs au chemin de déploiement de l'application.

Exemple : `http://localhost:8080/ecodrop/`

L'API peut répondre en **JSON** ou en **XML**. Le format souhaité doit être spécifié dans l'en-tête `Accept` de la requête.
- `Accept: application/json` (par défaut)
- `Accept: application/xml`

Pour les requêtes `POST`, `PUT` et `PATCH`, l'en-tête `Content-Type` doit être défini en conséquence.
- `Content-Type: application/json`
- `Content-Type: application/xml`

---

## Types de Déchets (Waste Types)

Le point de terminaison `/waste-types` est utilisé pour gérer les types de déchets.

### Modèle de Données : `WasteType`

| Champ         | Type    | Description                           |
|---------------|---------|---------------------------------------|
| `id`          | Integer | L'identifiant unique pour le type de déchet. |
| `nom`         | String  | Le nom du type de déchet.             |
| `pointsPerKilo`| Integer | Le nombre de points attribués par kilo.    |


### Opérations

#### 1. Obtenir tous les types de déchets

* **Méthode :** `GET`
* **Chemin :** `/waste-types`
* **Description :** Récupère une liste de tous les types de déchets.
* **Réponse en cas de succès :**
    * **Code :** `200 OK`
    * **Contenu :** Un tableau d'objets `WasteType`.
    * **Exemple (JSON) :**
      ```json
      [
          {
              "id": 1,
              "nom": "Verre",
              "pointsPerKilo": 10
          },
          {
              "id": 2,
              "nom": "Carton",
              "pointsPerKilo": 5
          }
      ]
      ```
* **Réponse si vide :**
    * **Code :** `204 No Content` s'il n'existe aucun type de déchet.

---

#### 2. Obtenir un type de déchet par ID

* **Méthode :** `GET`
* **Chemin :** `/waste-types/{id}`
* **Description :** Récupère un seul type de déchet par son ID.
* **Paramètres :**
    * `{id}` (entier, requis) : L'ID du type de déchet à récupérer.
* **Réponse en cas de succès :**
    * **Code :** `200 OK`
    * **Contenu :** Un objet `WasteType`.
    * **Exemple (JSON) :**
      ```json
      {
          "id": 1,
          "nom": "Verre",
          "pointsPerKilo": 10
      }
      ```
* **Réponses d'erreur :**
    * **Code :** `404 Not Found` si le type de déchet n'existe pas.
    * **Code :** `400 Bad Request` si l'ID est invalide.

---

#### 3. Créer un nouveau type de déchet

* **Méthode :** `POST`
* **Chemin :** `/waste-types`
* **Description :** Crée un nouveau type de déchet. L'`id` doit être omis du corps de la requête car il sera généré automatiquement.
* **Corps de la requête :** Un objet `WasteType`.
    * **Exemple (JSON) :**
      ```json
      {
          "nom": "Plastique",
          "pointsPerKilo": 8
      }
      ```
* **Réponse en cas de succès :**
    * **Code :** `201 Created`
    * **Contenu :** L'objet `WasteType` nouvellement créé, incluant son nouvel `id`.
    * **Exemple (JSON) :**
      ```json
      {
          "id": 3,
          "nom": "Plastique",
          "pointsPerKilo": 8
      }
      ```
* **Réponse d'erreur :**
    * **Code :** `400 Bad Request` si le corps de la requête est malformé.

---

#### 4. Mettre à jour un type de déchet

* **Méthode :** `PUT`
* **Chemin :** `/waste-types/{id}`
* **Description :** Met à jour un type de déchet existant. L'objet `WasteType` complet doit être fourni dans le corps de la requête, y compris l'`id`, qui doit correspondre à celui de l'URL.
* **Paramètres :**
    * `{id}` (entier, requis) : L'ID du type de déchet à mettre à jour.
* **Corps de la requête :** Un objet `WasteType`.
    * **Exemple (JSON) :**
      ```json
      {
          "id": 3,
          "nom": "Plastique Recyclable",
          "pointsPerKilo": 9
      }
      ```
* **Réponse en cas de succès :**
    * **Code :** `200 OK`
    * **Contenu :** L'objet `WasteType` mis à jour.
* **Réponses d'erreur :**
    * **Code :** `404 Not Found` si le type de déchet n'existe pas.
    * **Code :** `400 Bad Request` si l'ID dans le corps ne correspond pas à l'ID dans l'URL, ou si le corps est malformé.

---

#### 5. Supprimer un type de déchet

* **Méthode :** `DELETE`
* **Chemin :** `/waste-types/{id}`
* **Description :** Supprime un type de déchet par son ID.
* **Paramètres :**
    * `{id}` (entier, requis) : L'ID du type de déchet à supprimer.
* **Réponse en cas de succès :**
    * **Code :** `204 No Content`
* **Réponses d'erreur :**
    * **Code :** `404 Not Found` si le type de déchet n'existe pas.
    * **Code :** `409 Conflict` si le type de déchet ne peut pas être supprimé (par exemple, en raison de contraintes de clé étrangère).
    * **Code :** `400 Bad Request` si l'ID est invalide.

---
---

## Points de Collecte (Collection Points)

Le point de terminaison `/points` est utilisé pour gérer les points de collecte.

### Modèle de Données : `CollectionPoint`

| Champ       | Type    | Description                               |
|-------------|---------|-------------------------------------------|
| `id`        | Integer | L'identifiant unique pour le point de collecte.      |
| `adresse`   | String  | L'adresse du point de collecte.      |
| `capaciteMax`| Integer | La capacité maximale de déchets du point. |


### Opérations

#### 1. Obtenir tous les points de collecte

* **Méthode :** `GET`
* **Chemin :** `/points`
* **Description :** Récupère une liste de tous les points de collecte.
* **Réponse en cas de succès :**
    * **Code :** `200 OK`
    * **Contenu :** Un tableau d'objets `CollectionPoint`.
    * **Exemple (JSON) :**
      ```json
      [
          {
              "id": 1,
              "adresse": "123 Rue de la Soif",
              "capaciteMax": 500
          },
          {
              "id": 2,
              "adresse": "456 Avenue des Ecrins",
              "capaciteMax": 1000
          }
      ]
      ```
* **Réponse si vide :**
    * **Code :** `204 No Content` s'il n'existe aucun point de collecte.

---

#### 2. Obtenir un point de collecte par ID

* **Méthode :** `GET`
* **Chemin :** `/points/{id}`
* **Description :** Récupère un seul point de collecte par son ID.
* **Paramètres :**
    * `{id}` (entier, requis) : L'ID du point de collecte à récupérer.
* **Réponse en cas de succès :**
    * **Code :** `200 OK`
    * **Contenu :** Un objet `CollectionPoint`.
    * **Exemple (JSON) :**
      ```json
      {
          "id": 1,
          "adresse": "123 Rue de la Soif",
          "capaciteMax": 500
      }
      ```
* **Réponses d'erreur :**
    * **Code :** `404 Not Found` si le point de collecte n'existe pas.
    * **Code :** `400 Bad Request` si l'ID est invalide.

---

#### 3. Mettre à jour partiellement un point de collecte

* **Méthode :** `PATCH`
* **Chemin :** `/points/{id}`
* **Description :** Met à jour partiellement un point de collecte existant. Seuls les champs présents dans le corps de la requête seront mis à jour.
* **Paramètres :**
    * `{id}` (entier, requis) : L'ID du point de collecte à mettre à jour.
* **Corps de la requête :** Un objet `CollectionPoint` partiel.
    * **Exemple (JSON pour mettre à jour uniquement `capaciteMax`) :**
      ```json
      {
          "capaciteMax": 750
      }
      ```
* **Réponse en cas de succès :**
    * **Code :** `200 OK`
    * **Contenu :** L'objet `CollectionPoint` mis à jour.
* **Réponses d'erreur :**
    * **Code :** `404 Not Found` si le point de collecte n'existe pas.
    * **Code :** `400 Bad Request` si le corps de la requête est malformé.