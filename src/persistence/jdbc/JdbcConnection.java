package persistence.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class JdbcConnection {

    private static String host = "localhost";
    private static String port = "3306";
    private static String base = "tahiti_travel"; // ✅ même base que dans ton 1er code
    private static String user = "root";
    private static String password = ""; // ✅ mot de passe vide

    private static String url =
            "jdbc:mysql://" + host + ":" + port + "/" + base +
            "?useSSL=false&serverTimezone=UTC";

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("✅ JDBC Connection successful!");
            } catch (Exception e) {
                System.err.println("❌ JDBC Connection failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return connection;
    }
}