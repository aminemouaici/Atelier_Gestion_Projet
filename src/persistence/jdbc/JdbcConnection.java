package persistence.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnection {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/tahiti_travel";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Retourne une NOUVELLE connexion à chaque appel.
     * DOIT être fermée après usage (try-with-resources).
     */
    public static Connection getConnection() throws SQLException {
        // ✅ Crée une NOUVELLE instance à chaque appel
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}