package dao;

import db.Database;
import dto.CollectionPoint;
import dto.Deposit;
import dto.WasteType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCDepositDAO implements DepositDAO {
    private static Database bdd = new Database();
    public static final int DEFAULT_LIMIT = 20;
    public static final int DEFAULT_OFFSET = 0;
    private Deposit mapRow(ResultSet rs) throws SQLException {
        int userId = rs.getInt(2);
        CollectionPoint point = new CollectionPoint(rs.getInt(3), rs.getString(4), rs.getInt(5));
        WasteType wasteType = new WasteType(rs.getInt(6), rs.getString(7), rs.getInt(8));
        return new Deposit(rs.getInt(1), userId, point, wasteType, rs.getDouble(9), rs.getDate(10), rs.getBoolean(11));
    }

    @Override
    public List<Deposit> findAll() {
        return findAll(DEFAULT_LIMIT, DEFAULT_OFFSET);
    }

    @Override
    public List<Deposit> findAll(int limit, int offset) {
        List<Deposit> deposits = new ArrayList<>();
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT d.id, d.userid, cp.id, cp.adresse, cp.capaciteMax, wt.id, wt.nom, wt.pointsPerKilo, d.poids, d.datedepot " +
                                                            "FROM Deposit d " +
                                                            "JOIN CollectionPoint cp ON d.pointid = cp.id " +
                                                            "JOIN WasteType wt ON d.wasteTypeId = wt.id " +
                                                            "LIMIT ? OFFSET ?"
            );
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                deposits.add(mapRow(rs));
            }
        } catch (SQLException e) {
            return null;
        }
        return deposits;
    }

    @Override
    public List<Deposit> findAll(int limit) {
        return findAll(limit, DEFAULT_OFFSET);
    }

    /**
     * Retourne le poids total des dépôts pour un point de collecte donné.
     */
    public double getTotalWeightByPoint(int pointId) {
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT SUM(poids) FROM Deposit WHERE pointid = ?");
            ps.setInt(1, pointId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // SUM retourne NULL s'il n'y a aucun dépôt, wasNull() permet de le détecter
                double total = rs.getDouble(1);
                if (rs.wasNull()) return 0;
                return total;
            }
        } catch (SQLException e) {
            return -1;
        }
        return 0;
    }

    /**
     * Vérifie si l'ajout du dépôt ne dépasse pas la capacité du point de collecte.
     * @return true si le dépôt peut être accepté, false sinon.
     */
    public boolean canAcceptDeposit(int pointId, double weight) {
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "SELECT cp.capaciteMax, COALESCE(SUM(d.poids), 0) AS totalWeight " +
                "FROM CollectionPoint cp LEFT JOIN Deposit d ON d.pointid = cp.id AND d.collected IS FALSE " +
                "WHERE cp.id = ? GROUP BY cp.capaciteMax"
            );
            ps.setInt(1, pointId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int capaciteMax = rs.getInt(1);
                double totalWeight = rs.getDouble(2);
                return (totalWeight + weight) <= capaciteMax;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    @Override
    public Deposit save(Deposit deposit) {
        int pointId = deposit.getPoint().getId();
        double weight = deposit.getWeight();

        // Vérifier que le dépôt ne dépasse pas la capacité du point de collecte
        if (!canAcceptDeposit(pointId, weight)) {
            throw new IllegalStateException("Le poids total des dépôts dépasse la capacité maximale du point de collecte");
        }

        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO deposit(userid, pointid, wasteTypeId, poids, datedepot) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, deposit.getUserId());
            ps.setInt(2, deposit.getPoint().getId());
            ps.setInt(3, deposit.getWasteType().id());
            ps.setDouble(4, deposit.getWeight());
            ps.setDate(5, new Date(deposit.getDateDepot().getTime()));
            int affected = ps.executeUpdate();
            if (affected == 0) return null;
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int generatedId = keys.getInt(1);
                return findById(generatedId);
            }
            return null;
        } catch (SQLException e) {
            if (e.getMessage().contains("fk_deposit_accepts")) throw new IllegalStateException("Le point d'accès n'accepte pas ce type de déchets");
            return null;
        }
    }

    @Override
    public Deposit update(Deposit deposit) {
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE deposit SET userid = ?, pointid = ?, wasteTypeId = ?, poids = ?, datedepot = ?, collected = ? WHERE id = ?", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, deposit.getUserId());
            ps.setInt(2, deposit.getPoint().getId());
            ps.setInt(3, deposit.getWasteType().id());
            ps.setDouble(4, deposit.getWeight());
            ps.setDate(5, new Date(deposit.getDateDepot().getTime()));
            ps.setBoolean(6, deposit.getCollected());
            ps.setInt(7, deposit.getId());
            int affected = ps.executeUpdate();
            if (affected == 0) return null;
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                CollectionPointDAO collectionPointDAO = new JDBCCollectionPointDAO();
                WasteTypeDAO wasteTypeDAO = new JDBCWasteTypeDAO();
                CollectionPoint point = collectionPointDAO.findById(rs.getInt(3));
                WasteType wasteType = wasteTypeDAO.findById(rs.getInt(4));
                return new Deposit(rs.getInt(1), rs.getInt(2), point, wasteType, rs.getDouble(5), rs.getDate(6), rs.getBoolean(7));
            }
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public Deposit findById(int id) {
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT d.id, d.userid, cp.id, cp.adresse, cp.capaciteMax, wt.id, wt.nom, wt.pointsPerKilo, d.poids, d.datedepot, d.collected " +
                                                            "FROM Deposit d " +
                                                            "JOIN CollectionPoint cp ON d.pointid = cp.id " +
                                                            "JOIN WasteType wt ON d.wasteTypeId = wt.id " +
                                                            "WHERE d.id = ?"
            );
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }
}
