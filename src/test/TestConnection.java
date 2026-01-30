package test;

import java.sql.Connection;
import java.sql.SQLException;

import persistence.jdbc.JdbcConnection;

public class TestConnection {

    public static void main(String[] args) throws SQLException {
        Connection c = JdbcConnection.getConnection();

        if (c != null) {
            System.out.println("✅ JDBC CONNECTED");
        } else {
            System.out.println("❌ JDBC NOT CONNECTED");
        }
    }
}