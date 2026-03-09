package dao;

import dto.LeaderBoardUser;
import dto.User;

import java.util.Collection;

public interface UserDAO {
    Collection<LeaderBoardUser> leaderboard();
    User update(User user);

    Collection<LeaderBoardUser> leaderboard(int limit, int offset);

    Collection<LeaderBoardUser> leaderboard(int limit);
    User findById(int id);
}
