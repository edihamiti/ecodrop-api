package dto;

// Non-utilisation d'un record pour pouvoir utiliser la methode merge de MergeUtils (record met les attributs en final)
public class Deposit {
    private int id;
    private int userId;
    private int pointId;
    private double weight;

    public Deposit() {
    }

    public Deposit(int id, int userId, int pointId, double weight) {
        this.id = id;
        this.userId = userId;
        this.weight = weight;
        this.pointId = pointId;
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

    public int getPointId() {
        return pointId;
    }

    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
