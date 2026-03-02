package dto;

import java.io.Serializable;

public record WasteType(Integer id, String nom, int pointsPerKilo) implements Serializable {
}
