package persistence.bda;

import java.sql.ResultSet;
import java.util.*;

/**
 * Opérateur de jointure pour requêtes mixtes SQL-Text.
 * CONFORME AU PLAN 1 DU CAHIER DES CHARGES (page 8) :
 * 
 * Exécution :
 * 1. SQLOperator : exécute la requête SQL
 * 2. TextOperator : exécute la recherche Lucene sur répertoire R
 * 3. JoinOperator : jointure sur la clé c (id_site)
 * 4. Sort : tri par score Lucene décroissant
 * 
 * HYPOTHÈSES (page 8) :
 * - Résultats textuels petits (gardés en mémoire)
 * - Résultats SQL potentiellement grands (itération)
 * - Résultats finaux petits (gardés en mémoire)
 * 
 * @author Équipe Persistance
 */
public class JoinedOperator implements Operator<Map.Entry<Integer, Double>> {
    
    private final String keyCol;
    private final String docsDir;
    
    private final SqlOperator sqlOp = new SqlOperator();
    private final TextualOperator txtOp;
    
    private LinkedHashMap<Integer, Double> resultJoined = new LinkedHashMap<>();
    private Iterator<Map.Entry<Integer, Double>> it;
    
    /**
     * Constructeur.
     * 
     * @param keyCol Nom de la colonne clé (ex: "id_site")
     * @param docsDir Répertoire R contenant les fichiers .txt
     */
    public JoinedOperator(String keyCol, String docsDir) {
        this.keyCol = keyCol;
        this.docsDir = docsDir;
        this.txtOp = new TextualOperator(keyCol, docsDir);
    }
    
    @Override
    public void init(String query) {
        resultJoined = new LinkedHashMap<>();
        
        // Séparer la requête mixte : "SELECT ... FROM ... WITH mot-clés"
        String[] parts = query.split("(?i)with", 2);  // (?i) = case insensitive
        String sqlPart = parts[0].trim();
        String textPart = (parts.length > 1) ? parts[1].trim() : "";
        
        // S'assurer que la clé est sélectionnée (exigence BDA)
        if (!containsSelectKey(sqlPart, keyCol)) {
            sqlPart = injectKeyInSelect(sqlPart, keyCol);
        }
        
        // ===== ÉTAPE 1 : OPÉRATEUR TEXTUEL =====
        // Recherche Lucene sur fichiers du répertoire R
        txtOp.init(textPart);
        Map<Integer, Float> txtScores = txtOp.getScores(); // Déjà trié score desc
        
        // ===== ÉTAPE 2 : OPÉRATEUR SQL =====
        // Exécution requête SQL
        sqlOp.init(sqlPart);
        ResultSet rs = sqlOp.getResultSet();
        
        // ===== ÉTAPE 3 : OPÉRATEUR DE JOINTURE =====
        // Intersection sur la clé c (id_site)
        // Hypothèse BDA : résultats texte en mémoire, SQL potentiellement volumineux
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
        
        // ===== ÉTAPE 4 : TRI PAR SCORE =====
        // Construire résultat final dans l'ordre du texte (score desc)
        for (Map.Entry<Integer, Float> e : txtScores.entrySet()) {
            if (sqlIds.contains(e.getKey())) {
                resultJoined.put(e.getKey(), (double) e.getValue());
            }
        }
        
        it = resultJoined.entrySet().iterator();
    }
    
    /**
     * Vérifie si la requête SQL contient la clé dans le SELECT.
     */
    private boolean containsSelectKey(String sql, String key) {
        String lower = sql.toLowerCase(Locale.ROOT);
        return lower.contains("select") && lower.contains(key.toLowerCase(Locale.ROOT));
    }
    
    /**
     * Injecte la clé dans le SELECT si elle est manquante.
     */
    private String injectKeyInSelect(String sql, String key) {
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
    
    /**
     * Retourne tous les résultats de la jointure.
     * 
     * @return Map<id, score> triée par score décroissant
     */
    public LinkedHashMap<Integer, Double> getResultJoined() {
        return resultJoined;
    }
    
    @Override
    public void close() {
        sqlOp.close();
        txtOp.close();
    }
}
