package dto;

import java.io.Serializable;

/**
 * DTO représentant l'état de remplissage d'un point de collecte.
 * Utilisé pour GET /points/{id}/status et GET /points/overloaded
 */
public class CollectionPointStatus implements Serializable {
    private int id;
    private String adresse;
    private double fillRate;   // taux de remplissage en pourcentage
    private boolean full;      // true si le taux dépasse 100%

    public CollectionPointStatus() {}

    public CollectionPointStatus(int id, String adresse, double fillRate, boolean full) {
        this.id = id;
        this.adresse = adresse;
        this.fillRate = fillRate;
        this.full = full;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public double getFillRate() { return fillRate; }
    public void setFillRate(double fillRate) { this.fillRate = fillRate; }

    public boolean isFull() { return full; }
    public void setFull(boolean full) { this.full = full; }
}

