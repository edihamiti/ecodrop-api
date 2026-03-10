package dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO représentant un point de collecte avec la liste des types de déchets acceptés.
 * Utilisé pour GET /points/{id}
 */
public class CollectionPointWithWasteTypes implements Serializable {
    private int id;
    private String adresse;
    private int capaciteMax;
    private List<WasteType> acceptedWasteTypes;

    public CollectionPointWithWasteTypes() {}

    public CollectionPointWithWasteTypes(int id, String adresse, int capaciteMax, List<WasteType> acceptedWasteTypes) {
        this.id = id;
        this.adresse = adresse;
        this.capaciteMax = capaciteMax;
        this.acceptedWasteTypes = acceptedWasteTypes;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public int getCapaciteMax() { return capaciteMax; }
    public void setCapaciteMax(int capaciteMax) { this.capaciteMax = capaciteMax; }

    public List<WasteType> getAcceptedWasteTypes() { return acceptedWasteTypes; }
    public void setAcceptedWasteTypes(List<WasteType> acceptedWasteTypes) { this.acceptedWasteTypes = acceptedWasteTypes; }
}

