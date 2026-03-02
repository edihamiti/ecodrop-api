package dto;

import java.io.Serializable;

// Non-utilisation d'un record pour pouvoir utiliser la methode merge de MergeUtils (record met les attributs en final)
public class CollectionPoint implements Serializable {
    private Integer id;
    private String adresse;
    private Integer capaciteMax;

    public CollectionPoint(Integer id, String adresse, Integer capaciteMax) {
        this.id = id;
        this.adresse = adresse;
        this.capaciteMax = capaciteMax;
    }

    CollectionPoint(){}

    public Integer getId() {
        return id;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setCapaciteMax(Integer capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    public Integer getCapaciteMax() {
        return capaciteMax;
    }

}
