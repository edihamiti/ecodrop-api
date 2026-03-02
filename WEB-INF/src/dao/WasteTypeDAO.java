package dao;

import dto.WasteType;

import java.util.List;

public interface WasteTypeDAO {
    public WasteType findById(int id);
    public List<WasteType> findAll();

    /**
     * Sauvegarde le wasteType en générant un ID unique
     * @param wasteType
     * @return le wasteType sauvegardé avec l'ID généré
     */
    public WasteType save(WasteType wasteType);

    /**
     * Mets à jour un WasteType
     *
     * @param wasteType
     * @return true si la modification s'est bien déroulée, false sinon
     */
    public WasteType update(WasteType wasteType);
}
