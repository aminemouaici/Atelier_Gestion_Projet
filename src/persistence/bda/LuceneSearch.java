package persistence.bda;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Moteur de recherche Lucene.
 * Effectue des recherches sur un index existant.
 * 
 * @author Équipe Persistance
 */
public class LuceneSearch {
    
    private final Analyzer analyzer = new FrenchAnalyzer();
    private final Directory indexDir;
    private final String keyField;
    private static final int MAX_RESULTS = 100;
    
    /**
     * Constructeur.
     * 
     * @param keyField Nom du champ contenant la clé (ex: "id_site")
     * @param indexPath Chemin de l'index Lucene
     * @throws Exception Si l'index n'existe pas
     */
    public LuceneSearch(String keyField, Path indexPath) throws Exception {
        this.keyField = keyField;
        this.indexDir = FSDirectory.open(indexPath);
    }
    
    /**
     * Effectue une recherche.
     * 
     * @param queryText Texte de la requête (ex: "musée culture")
     * @return Map<id, score> triée par score décroissant (LinkedHashMap)
     * @throws Exception Si erreur lors de la recherche
     */
    public Map<Integer, Float> search(String queryText) throws Exception {
        Map<Integer, Float> scores = new LinkedHashMap<>();
        
        try (DirectoryReader reader = DirectoryReader.open(indexDir)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser qp = new QueryParser("description", analyzer);
            Query q = qp.parse(queryText == null ? "" : queryText);
            
            TopDocs results = searcher.search(q, MAX_RESULTS);
            
            for (ScoreDoc sd : results.scoreDocs) {
                Document d = searcher.doc(sd.doc);
                int id = Integer.parseInt(d.get(keyField));
                scores.put(id, sd.score);
            }
        }
        
        return scores;
    }
    
    public Analyzer getAnalyzer() {
        return analyzer;
    }
    
    public String getKeyField() {
        return keyField;
    }
}
