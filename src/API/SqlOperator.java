package API;

import persistence.jdbc.JdbcExecuteQuery;

import java.sql.ResultSet;

public class SqlOperator implements Operator<ResultSet> {

    private final JdbcExecuteQuery exec = new JdbcExecuteQuery();
    private ResultSet rs;

    @Override
    public void init(String sqlQuery) {
        exec.prepareQuery(sqlQuery);
        rs = exec.sqlExecutePreparedQuery();
    }

    @Override
    public ResultSet next() {
        try {
            if (rs == null) return null;
            if (rs.next()) return rs;
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public ResultSet getResultSet() {
        return rs;
    }

    public JdbcExecuteQuery getJdbcExecuteQuery() {
        return exec;
    }

    @Override
    public void close() {
        exec.close();
    }
}

