# Types

Les différents types d'objets retournés par l'API EcoDrop.

## WasteType

Un type de déchets accepté par les points de collecte.

| Champ | Type | Description |
|-------|------|-------------|
| `id` | `number` | Identifiant unique du type de déchet |
| `nom` | `string` | Nom du type de déchet (ex: "plastiques", "verre", "métaux") |
| `pointsPerKilo` | `number` | Points gagnés par kilogramme de ce déchet collecté |

## CollectionPoint

Un point de collecte de déchets.

| Champ | Type | Description |
|-------|------|-------------|
| `id` | `number` | Identifiant unique du point de collecte |
| `adresse` | `string` | Adresse complète du point de collecte |
| `capaciteMax` | `number` | Capacité maximale en kilogrammes |

## CollectionPointWithWasteTypes

Un point de collecte avec la liste des types de déchets acceptés. Retourné lors de la consultation détaillée d'un point.

| Champ | Type | Description |
|-------|------|-------------|
| `id` | `number` | Identifiant unique du point de collecte |
| `adresse` | `string` | Adresse complète du point de collecte |
| `capaciteMax` | `number` | Capacité maximale en kilogrammes |
| `acceptedWasteTypes` | `WasteType[]` | Liste des types de déchets acceptés par ce point |

## CollectionPointStatus

État de remplissage d'un point de collecte. Retourné pour consulter le statut et identifier les points surcharges.

| Champ | Type | Description |
|-------|------|-------------|
| `id` | `number` | Identifiant unique du point de collecte |
| `adresse` | `string` | Adresse complète du point de collecte |
| `fillRate` | `number` | Taux de remplissage en pourcentage (0-100+) |
| `full` | `boolean` | `true` si le taux dépasse 100% (point surcharge) |

## Deposit

Un dépôt de déchets effectué par un utilisateur.

| Champ | Type | Description |
|-------|------|-------------|
| `id` | `number` | Identifiant unique du dépôt |
| `userId` | `number` | Identifiant de l'utilisateur qui a effectué le dépôt |
| `point` | `CollectionPoint` | Point de collecte où les déchets ont été déposés |
| `wasteType` | `WasteType` | Type de déchet déposé |
| `weight` | `number` | Poids en kilogrammes |
| `dateDepot` | `Date` | Date et heure du dépôt |
| `collected` | `boolean` | `true` si les déchets ont déjà été collectés |

## User

Un utilisateur du système.

| Champ | Type | Description |
|-------|------|-------------|
| `id` | `number` | Identifiant unique de l'utilisateur |
| `login` | `string` | Identifiant de connexion unique |
| `role` | `string` | Rôle de l'utilisateur (`USER` ou `ADMIN`) |
