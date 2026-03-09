package dao;

import bdd.DS;
import dto.CollectionPoint;
import dto.LeaderBoardUser;
import dto.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JDBCUsersDAO implements UsersDAO {
    private static final DS bdd = new DS();

    @Override
    public Users update( Users user) {
        try (Connection con = bdd.getConnection()){
            PreparedStatement ps = con.prepareStatement("UPDATE User SET login = ?, password = ?, role = ? WHERE id = ?");
            ps.setInt(4, user.getId());
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();
        } catch (SQLException e) {
            return null;
        }
        return user;
    }

    @Override
    public List<LeaderBoardUser> leaderboard() {
        List<LeaderBoardUser> users = new java.util.ArrayList<>();
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT Users.login, SUM(Deposit.poids * WasteType.pointsPerKilo) AS totalPoints\n" +
                    "FROM Users\n" +
                    "JOIN Deposit ON Users.id = Deposit.userId\n" +
                    "JOIN WasteType ON Deposit.wasteTypeId = WasteType.id\n" +
                    "GROUP BY Users.id, Users.login\n" +
                    "ORDER BY totalPoints DESC\n" +
                    "LIMIT 10");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(new LeaderBoardUser(rs.getString(1), rs.getInt(2)));
            }
        } catch (SQLException e) {
            return null;
        }
        return users;
    }
}
