package API;

import java.sql.ResultSet;
import java.util.*;

/**
 * Opérateur de jointure (plan 1) pour requêtes mixtes "... with ..."
 * Retourne Map<id, score> ordonné par score desc (ordre Lucene).
 */
public class JoinedOperator implements Operator<Map.Entry<Integer, Double>> {

    private final String keyCol;
    private final String docsDir;

    private final SqlOperator sqlOp = new SqlOperator();
    private final TextualOperator txtOp;

    private LinkedHashMap<Integer, Double> resultJoined = new LinkedHashMap<>();
    private Iterator<Map.Entry<Integer, Double>> it;

    public JoinedOperator(String keyCol, String docsDir) {
        this.keyCol = keyCol;
        this.docsDir = docsDir;
        this.txtOp = new TextualOperator(keyCol, docsDir);
    }

    @Override
    public void init(String query) {
        resultJoined = new LinkedHashMap<>();

        String[] parts = query.split("with", 2);
        String sqlPart = parts[0].trim();
        String textPart = (parts.length > 1) ? parts[1].trim() : "";

        // S'assure que la clé est sélectionnée (exigence BDA)
        if (!containsSelectKey(sqlPart, keyCol)) {
            sqlPart = injectKeyInSelect(sqlPart, keyCol);
        }

        // 1) SQL
        sqlOp.init(sqlPart);
        ResultSet rs = sqlOp.getResultSet();

        // 2) Texte
        txtOp.init(textPart);
        Map<Integer, Float> txtScores = txtOp.getScores(); // déjà ordonné score desc

        // 3) Jointure : intersection sur la clé
        // Hypothèse BDA : résultats texte en mémoire, SQL potentiellement volumineux :contentReference[oaicite:3]{index=3}
        Set<Integer> txtIds = txtScores.keySet();
        Set<Integer> sqlIds = new HashSet<>();

        try {
            while (rs.next()) {
                sqlIds.add(rs.getInt(keyCol));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlOp.close();
        }

        // Construit résultat final dans l'ordre du texte (score desc)
        for (Map.Entry<Integer, Float> e : txtScores.entrySet()) {
            if (sqlIds.contains(e.getKey())) {
                resultJoined.put(e.getKey(), (double) e.getValue());
            }
        }

        it = resultJoined.entrySet().iterator();
    }

    private boolean containsSelectKey(String sql, String key) {
        String lower = sql.toLowerCase(Locale.ROOT);
        return lower.contains("select") && lower.contains(key.toLowerCase(Locale.ROOT));
    }

    private String injectKeyInSelect(String sql, String key) {
        // très simple : "SELECT ..." -> "SELECT key, ..."
        String lower = sql.toLowerCase(Locale.ROOT);
        int idx = lower.indexOf("select");
        if (idx < 0) return sql;

        int afterSelect = idx + "select".length();
        return sql.substring(0, afterSelect) + " " + key + ", " + sql.substring(afterSelect).trim();
    }

    @Override
    public Map.Entry<Integer, Double> next() {
        if (it != null && it.hasNext()) return it.next();
        return null;
    }

    public LinkedHashMap<Integer, Double> getResultJoined() {
        return resultJoined;
    }

    @Override
    public void close() {
        sqlOp.close();
        txtOp.close();
    }
}

