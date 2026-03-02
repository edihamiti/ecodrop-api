package bdd;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DS {

    private final String DRIVER;
    private final String URL;
    private final String LOGIN;
    private final String PASSWORD;

    public DS() {
        try {
            Properties properties = new Properties();
            InputStream input = this.getClass().getClassLoader().getResourceAsStream("config.prop");
            if (input == null) {
                throw new RuntimeException("Unable to find config.prop");
            }
            properties.load(input);
            DRIVER = properties.getProperty("driver");
            URL = properties.getProperty("url");
            LOGIN = properties.getProperty("login");
            PASSWORD = properties.getProperty("password");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, LOGIN, PASSWORD);

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
