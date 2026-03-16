package dao;

import db.Database;
import dto.CollectionPoint;
import dto.CollectionPointStatus;
import dto.CollectionPointWithWasteTypes;
import dto.WasteType;
import utils.Config;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCCollectionPointDAO implements CollectionPointDAO{
    private static Database bdd = new Database();

    @Override
    public CollectionPoint findById(int id) {
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM collectionpoint WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new CollectionPoint(rs.getInt(1), rs.getString(2), rs.getInt(3));
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    @Override
    public List<CollectionPoint> findAll() {
        return findAll(Integer.parseInt(Config.get("default_limit")), Integer.parseInt(Config.get("default_offset")));
    }

    @Override
    public List<CollectionPoint> findAll(int limit, int offset) {
        List<CollectionPoint> collectionPoints = new ArrayList<>();
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM collectionpoint LIMIT ? OFFSET ?");
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                collectionPoints.add(new CollectionPoint(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
        } catch (SQLException e) {
            return null;
        }
        return collectionPoints;
    }

    @Override
    public List<CollectionPoint> findAll(int limit) {
        return findAll(limit, Integer.parseInt(Config.get("default_offset")));
    }

    @Override
    public CollectionPoint update(CollectionPoint collectionPoint) {
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "UPDATE collectionpoint SET adresse = ?, capacitemax = ? WHERE id = ?");
            ps.setString(1, collectionPoint.getAdresse());
            ps.setInt(2, collectionPoint.getCapaciteMax());
            ps.setInt(3, collectionPoint.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            return null;
        }
        return collectionPoint;
    }

    @Override
    public CollectionPointWithWasteTypes findByIdWithWasteTypes(int id) {
        CollectionPoint cp = findById(id);
        if (cp == null) return null;

        List<WasteType> wasteTypes = new ArrayList<>();
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "SELECT wt.id, wt.nom, wt.pointsPerKilo " +
                "FROM Accepts a JOIN WasteType wt ON a.wastetypeid = wt.id " +
                "WHERE a.pointid = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                wasteTypes.add(new WasteType(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
        } catch (SQLException e) {
            return null;
        }
        return new CollectionPointWithWasteTypes(cp.getId(), cp.getAdresse(), cp.getCapaciteMax(), wasteTypes);
    }

    @Override
    public CollectionPointStatus getStatus(int id) {
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "SELECT cp.id, cp.adresse, cp.capaciteMax, COALESCE(SUM(d.poids), 0) AS totalPoids " +
                "FROM CollectionPoint cp " +
                "LEFT JOIN Deposit d ON d.pointid = cp.id " +
                "WHERE cp.id = ? " +
                "GROUP BY cp.id, cp.adresse, cp.capaciteMax");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int capaciteMax = rs.getInt(3);
                double totalPoids = rs.getDouble(4);
                double fillRate = capaciteMax > 0 ? (totalPoids / capaciteMax) * 100.0 : 0.0;
                boolean full = fillRate >= 100.0;
                return new CollectionPointStatus(rs.getInt(1), rs.getString(2), fillRate, full);
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    @Override
    public List<CollectionPointStatus> findOverloaded() {
        List<CollectionPointStatus> result = new ArrayList<>();
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                "SELECT cp.id, cp.adresse, cp.capaciteMax, COALESCE(SUM(d.poids), 0) AS totalPoids " +
                "FROM CollectionPoint cp " +
                "LEFT JOIN Deposit d ON d.pointid = cp.id " +
                "GROUP BY cp.id, cp.adresse, cp.capaciteMax " +
                "HAVING cp.capaciteMax > 0 AND (COALESCE(SUM(d.poids), 0) * 100.0 / cp.capaciteMax) > 80");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int capaciteMax = rs.getInt(3);
                double totalPoids = rs.getDouble(4);
                double fillRate = (totalPoids / capaciteMax) * 100.0;
                boolean full = fillRate >= 100.0;
                result.add(new CollectionPointStatus(rs.getInt(1), rs.getString(2), fillRate, full));
            }
        } catch (SQLException e) {
            return null;
        }
        return result;
    }

    @Override
    public boolean clearDeposits(int pointId) {
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE deposit SET collected = TRUE WHERE pointId = ?");
            ps.setInt(1, pointId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
