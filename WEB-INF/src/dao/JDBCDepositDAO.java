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
    @Override
    public List<Deposit> findAll() {
        return findAll(DEFAULT_LIMIT, DEFAULT_OFFSET);
    }

    @Override
    public List<Deposit> findAll(int limit, int offset) {
        List<Deposit> deposits = new ArrayList<>();
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM collectionpoint LIMIT ? OFFSET ?");
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                deposits.add(new Deposit(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getDouble(4)));
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
            PreparedStatement ps = con.prepareStatement("select * from deposit where id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Deposit(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getDouble(4));
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }
}
