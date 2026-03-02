package dao;

import bdd.DS;
import dto.WasteType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCWasteTypeDAO implements WasteTypeDAO {
    private static DS bdd = new DS();

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
    public List<WasteType> findAll() {
        List<WasteType> wasteTypes = new ArrayList<>();
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("select * from wastetype");
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
    public boolean save(WasteType wasteType) {
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO wastetype VALUES (?, ?, ?)");
            ps.setInt(1, wasteType.id());
            ps.setString(2, wasteType.nom());
            ps.setInt(3, wasteType.pointsPerKilo());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
