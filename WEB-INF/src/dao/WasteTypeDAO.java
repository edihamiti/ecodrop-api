package dao;

import dto.WasteType;

import java.util.List;

public interface WasteTypeDAO {
    public WasteType findById(int id);
    public List<WasteType> findAll();
    public boolean save(WasteType wasteType);
}
