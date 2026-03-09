package dto;

// Non-utilisation d'un record pour pouvoir utiliser la methode merge de MergeUtils (record met les attributs en final)
public class Deposit {
    private int id;
    private int userId;
    private CollectionPoint point;


    private WasteType wasteType;
    private double weight;

    public Deposit() {
    }

    public Deposit(int id, int userId, CollectionPoint point, WasteType wasteType, double weight) {
        this.id = id;
        this.userId = userId;
        this.weight = weight;
        this.wasteType = wasteType;
        this.point = point;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public CollectionPoint getPoint() {
        return point;
    }

    public void setPoint(CollectionPoint point) {
        this.point = point;
    }
    public WasteType getWasteType() {
        return wasteType;
    }

    public void setWasteType(WasteType wasteType) {
        this.wasteType = wasteType;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
