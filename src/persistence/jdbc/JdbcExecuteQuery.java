package persistence.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcExecuteQuery {
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public void prepareQuery(String query) {
        try {
            preparedStatement = JdbcConnection.getConnection().prepareStatement(query);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public ResultSet sqlExecutePreparedQuery() {
        try {
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return resultSet;
    }

    public int sqlExecutePreparedUpdate() {
        try {
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void close() {
        try {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
