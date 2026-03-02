package dto;

import java.io.Serializable;

public record CollectionPoint(int id, String adresse, int capaciteMax) implements Serializable {
}
