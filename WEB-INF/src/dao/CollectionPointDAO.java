package dao;

import dto.CollectionPoint;
import dto.WasteType;

import java.util.List;

public interface CollectionPointDAO {
    public CollectionPoint findById(int id);
    public List<CollectionPoint> findAll();
    public CollectionPoint update(CollectionPoint collectionPoint);
    public CollectionPoint delete(int id);
}
