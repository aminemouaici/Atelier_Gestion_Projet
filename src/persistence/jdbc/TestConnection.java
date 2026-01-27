package persistence.jdbc;

import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {
        Connection c = JdbcConnection.getConnection();

        if (c != null) {
            System.out.println("✅ JDBC CONNECTED");
        } else {
            System.out.println("❌ JDBC NOT CONNECTED");
        }
    }
}