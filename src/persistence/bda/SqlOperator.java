package persistence.bda;

import persistence.jdbc.JdbcExecuteQuery;

import java.sql.ResultSet;

/**
 * Opérateur pour les requêtes SQL pures.
 * Retourne un ResultSet JDBC.
 * 
 * Utilisé par JoinedOperator dans le Plan 1.
 * 
 * @author Équipe Persistance
 */
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
    
    /**
     * Retourne le ResultSet complet (pour jointure).
     * Utilisé par JoinedOperator.
     * 
     * @return ResultSet complet
     */
    public ResultSet getResultSet() {
        return rs;
    }
    
    /**
     * Retourne l'objet JdbcExecuteQuery (pour tests).
     * 
     * @return JdbcExecuteQuery interne
     */
    public JdbcExecuteQuery getJdbcExecuteQuery() {
        return exec;
    }
    
    @Override
    public void close() {
        exec.close();
    }
}
