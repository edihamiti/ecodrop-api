package dao;

import db.Database;
import dto.LeaderBoardUser;
import dto.User;
import dto.WasteType;
import utils.Config;

import java.sql.*;
import java.util.Collection;
import java.util.List;

public class JDBCUserDAO implements UserDAO {
    private static final Database bdd = new Database();

    @Override
    public User update(User user) {
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE Users SET login = ?, role = ? WHERE id = ?");
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getRole());
            ps.setInt(3, user.getId());
            int affected = ps.executeUpdate();
            if (affected == 0) return null;
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return new User(rs.getInt(1), rs.getString(2), rs.getString(3));
            }
            return null;
        } catch (SQLException e) {
            return null;
        }
    }


    public Collection<LeaderBoardUser> leaderboard(int limit) {
        return leaderboard(limit, Integer.parseInt(Config.get("default_offset")));
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
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Users WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt(1), rs.getString(2), rs.getString(3));
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    @Override
    public User findByLogin(String login) {
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Users WHERE login = ?");
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt(1), rs.getString(2), rs.getString(3));
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    @Override
    public User save(String login) {
        try (Connection con = bdd.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO Users(login, role) VALUES (?, 'USER')", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, login);
            int affected = ps.executeUpdate();
            if (affected == 0) return null;
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return new User(rs.getInt(1), rs.getString(2), rs.getString(2));
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

}
