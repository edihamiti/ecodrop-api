package dao;

import bdd.DS;
import dto.CollectionPoint;
import dto.Deposit;
import dto.WasteType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCDepositDAO implements DepositDAO {
    private static DS bdd = new DS();
    public static final int DEFAULT_LIMIT = 20;
    public static final int DEFAULT_OFFSET = 0;
    private Deposit mapRow(ResultSet rs) throws SQLException {
        int userId = rs.getInt(2);
        CollectionPoint point = new CollectionPoint(rs.getInt(3), rs.getString(4), rs.getInt(5));
        WasteType wasteType = new WasteType(rs.getInt(6), rs.getString(7), rs.getInt(8));
        return new Deposit(rs.getInt(1), userId, point, wasteType, rs.getDouble(9));
    }

    @Override
    public List<Deposit> findAll() {
        return findAll(DEFAULT_LIMIT, DEFAULT_OFFSET);
    }

    @Override
    public List<Deposit> findAll(int limit, int offset) {
        List<Deposit> deposits = new ArrayList<>();
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT d.id, d.userid, cp.id, cp.adresse, cp.capaciteMax, wt.id, wt.nom, wt.pointsPerKilo, d.poids " +
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

    @Override
    public Deposit save(Deposit deposit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Deposit update(Deposit deposit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Deposit findById(int id) {
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT d.id, d.userid, cp.id, cp.adresse, cp.capaciteMax, wt.id, wt.nom, wt.pointsPerKilo, d.poids " +
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
