package db;

import utils.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String DRIVER = Config.get("driver");
    private static final String URL = Config.get("url");
    private static final String LOGIN = Config.get("login");
    private static final String PASSWORD = Config.get("password");

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Driver introuvable : " + DRIVER);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, LOGIN, PASSWORD);
    }
}