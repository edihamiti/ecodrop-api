package dao;

import dto.Deposit;
import dto.WasteType;

import java.util.List;

public interface DepositDAO {
    public Deposit findById(int id);
    public List<Deposit> findAll();
    public List<Deposit> findAll(int limit, int offset);
    public List<Deposit> findAll(int limit);

    /**
     * Sauvegarde le Deposit en générant un ID unique
     * @return le Deposit sauvegardé avec l'ID généré
     */
    public Deposit save(Deposit deposit);

    /**
     * Mets à jour un Deposit
     *
     * @return true si la modification s'est bien déroulée, false sinon
     */
    public Deposit update(Deposit deposit);
}
