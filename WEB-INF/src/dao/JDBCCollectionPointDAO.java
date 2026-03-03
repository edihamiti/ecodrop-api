package dao;

import db.Database;
import dto.CollectionPoint;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCCollectionPointDAO implements CollectionPointDAO{
    private static Database bdd = new Database();

    @Override
    public CollectionPoint findById(int id) {
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("select * from collectionpoint where id = ?");
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
        List<CollectionPoint> collectionPoints = new ArrayList<>();
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM collectionpoint");
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
    public CollectionPoint update(CollectionPoint collectionPoint) {
        try (Connection con = bdd.getConnection()){
            PreparedStatement ps = con.prepareStatement("UPDATE collectionpoint SET adress = ?, capacitemax = ?  WHERE id = ?");
            ps.setInt(3, collectionPoint.getId());
            ps.setString(1, collectionPoint.getAdresse());
            ps.setInt(2, collectionPoint.getCapaciteMax());
            ps.executeUpdate();
        } catch (SQLException e) {
            return null;
        }
        return collectionPoint;
    }

    @Override
    public CollectionPoint delete(int id) {
        try (Connection con = bdd.getConnection()){
            PreparedStatement ps = con.prepareStatement("DELETE * FROM collectionpoint WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            return null;
        }
        return new CollectionPoint(null, null, null);
    }
}
