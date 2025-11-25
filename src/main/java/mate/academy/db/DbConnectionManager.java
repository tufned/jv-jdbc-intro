package mate.academy.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnectionManager {
    private static final String URL = "jdbc:mysql://localhost:3306/mate_academy";
    private static final Properties PROPERTIES;

    static {
        PROPERTIES = new Properties();
        PROPERTIES.put("user", "root");
        PROPERTIES.put("password", "qwerty1234");

        try {
            String className = "com.mysql.cj.jdbc.Driver";
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not load the JDBC driver", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, PROPERTIES);
        } catch (SQLException e) {
            throw new RuntimeException("Cannot create a connection to the DB", e);
        }
    }
}
