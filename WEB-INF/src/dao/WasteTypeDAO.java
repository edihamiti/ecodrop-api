package dao;

import dto.WasteType;

import java.util.List;

public interface WasteTypeDAO {
    public WasteType findById(int id);
    public List<WasteType> findAll();
    public boolean save(WasteType wasteType);

    /**
     * Sauvegarde le wasteType en générant un ID unique
     * @param wasteType
     * @return le wasteType sauvegardé avec l'ID généré
     */
    public WasteType save(WasteType wasteType);
}
