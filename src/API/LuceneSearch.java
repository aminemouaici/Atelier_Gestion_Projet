package API;

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

public class LuceneSearch {

    private final Analyzer analyzer = new FrenchAnalyzer();
    private final Directory indexDir;
    private final String keyField;
    private static final int MAX_RESULTS = 100;

    public LuceneSearch(String keyField, Path indexPath) throws Exception {
        this.keyField = keyField;
        this.indexDir = FSDirectory.open(indexPath);
    }

    /**
     * Retourne un LinkedHashMap (id -> score) dans l'ordre décroissant des scores.
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
