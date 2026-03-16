package dao;

import dto.CollectionPoint;
import dto.CollectionPointStatus;
import dto.CollectionPointWithWasteTypes;
import dto.WasteType;

import java.util.List;

public interface CollectionPointDAO {
    CollectionPoint findById(int id);
    List<CollectionPoint> findAll();
    List<CollectionPoint> findAll(int limit, int offset);
    List<CollectionPoint> findAll(int limit);
    CollectionPoint update(CollectionPoint collectionPoint);

    /** Retourne un point avec la liste imbriquée des WasteTypes acceptés */
    CollectionPointWithWasteTypes findByIdWithWasteTypes(int id);

    /** Retourne le statut de remplissage d'un point (taux en %, clé full) */
    CollectionPointStatus getStatus(int id);

    /** Liste les points dont le taux d'occupation est supérieur à 80% */
    List<CollectionPointStatus> findOverloaded();

    /** Vide tous les dépôts d'un point de collecte (action "clear") */
    boolean clearDeposits(int pointId);
}
