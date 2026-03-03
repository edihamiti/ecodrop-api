package dao;

import db.Database;
import dto.WasteType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCWasteTypeDAO implements WasteTypeDAO {
    private static final Database bdd = new Database();

    @Override
    public WasteType findById(int id) {
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("select * from wastetype where id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new WasteType(rs.getInt(1), rs.getString(2), rs.getInt(3));
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    @Override
    public List<WasteType> findAll(int limit, int offset) {
        List<WasteType> wasteTypes = new ArrayList<>();
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("select * from wastetype limit ? offset ?");
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                wasteTypes.add(new WasteType(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
        } catch (SQLException e) {
            return null;
        }
        return wasteTypes;
    }

    @Override
    public List<WasteType> findAll() {
        return findAll(DEFAULT_LIMIT, DEFAULT_OFFSET);
    }

    @Override
    public List<WasteType> findAll(int limit) {
        return findAll(limit, DEFAULT_OFFSET);
    }

    @Override
    public WasteType save(WasteType wasteType) {
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO wastetype(nom, pointsPerKilo) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, wasteType.nom());
            ps.setInt(2, wasteType.pointsPerKilo());
            int affected = ps.executeUpdate();
            if (affected == 0) return null;
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int generatedId = keys.getInt(1);
                return new WasteType(generatedId, wasteType.nom(), wasteType.pointsPerKilo());
            }
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public WasteType update(WasteType wasteType) {
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE wastetype SET nom = ?, pointsPerKilo = ? WHERE id = ?", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, wasteType.nom());
            ps.setInt(2, wasteType.pointsPerKilo());
            ps.setInt(3, wasteType.id());
            int affected = ps.executeUpdate();
            if (affected == 0) return null;
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return new WasteType(rs.getInt(1), rs.getString(2), rs.getInt(3));
            }
            return wasteType;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public boolean delete(int id) {
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM wastetype WHERE id = ?");
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(WasteType wasteType) {
        return delete(wasteType.id());
    }
}
