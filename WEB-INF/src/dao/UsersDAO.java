package dao;

import dto.LeaderBoardUser;
import dto.Users;

import java.util.List;

interface UsersDAO {
    List<LeaderBoardUser> leaderboard();
    Users update(Users user);
}
