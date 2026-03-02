package dto;

import java.io.Serializable;

public record CollectionPoint(Integer id, String adresse, Integer capaciteMax) implements Serializable {
}
