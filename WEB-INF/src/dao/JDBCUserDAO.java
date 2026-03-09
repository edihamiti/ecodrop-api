package dao;

import bdd.DS;
import dto.LeaderBoardUser;
import dto.User;
import utils.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class JDBCUserDAO implements UserDAO {
    private static final DS bdd = new DS();

    @Override
    public User update(User user) {
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


    public Collection<LeaderBoardUser> leaderboard(int limit) {
        return leaderboard(limit, 0);
    }

    public Collection<LeaderBoardUser> leaderboard(){
        return leaderboard(Integer.parseInt(Config.get("default_limit")), Integer.parseInt(Config.get("default_offset")));
    }

    public Collection<LeaderBoardUser> leaderboard(int limit, int offset) {
        List<LeaderBoardUser> users = new java.util.ArrayList<>();
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT Users.login, SUM(Deposit.poids * WasteType.pointsPerKilo) AS totalPoints\n" +
                    "FROM Users\n" +
                    "JOIN Deposit ON Users.id = Deposit.userId\n" +
                    "JOIN WasteType ON Deposit.wasteTypeId = WasteType.id\n" +
                    "GROUP BY Users.id, Users.login\n" +
                    "ORDER BY totalPoints DESC\n" +
                    "LIMIT ? OFFSET ?");
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(new LeaderBoardUser(rs.getString(1), rs.getInt(2)));
            }
        } catch (SQLException e) {
            return null;
        }
        return users;
    }

    @Override
    public User findById(int id) {
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM User WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }
}
