package videostats.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final String url;
    private final String user;
    private final String password;

    private static DatabaseConnection instance;

    private DatabaseConnection(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public static DatabaseConnection getInstance(String url, String user, String password) {
        if (instance == null) {
            instance = new DatabaseConnection(url, user, password);
        }
        return instance;
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            throw new IllegalStateException("DatabaseConnection not initialized.");
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public void releaseConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println("Error releasing connection: " + e.getMessage());
        }
    }
}
