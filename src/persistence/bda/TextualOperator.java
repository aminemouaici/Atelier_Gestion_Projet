package persistence.bda;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Opérateur textuel (Lucene).
 * Indexe les fichiers du répertoire R et effectue des recherches.
 * Retourne les résultats sous forme de Map<id, score>.
 * 
 * CONFORME AU PLAN 1 :
 * - Indexe les fichiers du répertoire R (id_site.txt)
 * - Retourne (clé c, score) triés par score décroissant
 * 
 * @author Équipe Persistance
 */
public class TextualOperator implements Operator<Map.Entry<Integer, Float>> {
    
    private final String keyField;
    private final String docsDir;
    
    // Index temporaire pour chaque recherche
    private final Path indexPath = FileSystems.getDefault().getPath(
        System.getProperty("java.io.tmpdir"), "lucene_index_tmp"
    );
    
    private LuceneSearch lucene;
    private Map<Integer, Float> scores = new LinkedHashMap<>();
    private Iterator<Map.Entry<Integer, Float>> it;
    
    /**
     * Constructeur.
     * 
     * @param keyField Nom du champ clé (ex: "id_site")
     * @param docsDir Répertoire R contenant les fichiers .txt
     */
    public TextualOperator(String keyField, String docsDir) {
        this.keyField = keyField;
        this.docsDir = docsDir;
    }
    
    @Override
    public void init(String textQuery) {
        try {
            buildIndex();
            lucene = new LuceneSearch(keyField, indexPath);
            scores = lucene.search(textQuery == null ? "" : textQuery);
            it = scores.entrySet().iterator();
        } catch (Exception e) {
            e.printStackTrace();
            scores = new LinkedHashMap<>();
            it = scores.entrySet().iterator();
        }
    }
    
    /**
     * Construit l'index Lucene à partir des fichiers du répertoire R.
     * CONFORME CAHIER DES CHARGES (page 7) :
     * - Fichiers nommés c.txt (ex: 1.txt, 2.txt, 3.txt)
     * - c = clé de la table T (id_site)
     * 
     * @throws Exception Si erreur lors de l'indexation
     */
    private void buildIndex() throws Exception {
        Files.createDirectories(indexPath);
        
        Directory dir = FSDirectory.open(indexPath);
        
        IndexWriterConfig config = new IndexWriterConfig(new org.apache.lucene.analysis.fr.FrenchAnalyzer());
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        
        IndexWriter w = null;
        try {
            w = new IndexWriter(dir, config);
            
            File folder = new File(docsDir);
            File[] files = folder.listFiles((d, name) -> name.endsWith(".txt"));
            if (files == null) return;
            
            for (File f : files) {
                // Extraire la clé c depuis le nom du fichier (ex: "1.txt" → 1)
                String filename = f.getName().replace(".txt", "");
                
                // Lire le contenu du fichier
                String content = new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
                
                Document doc = new Document();
                doc.add(new TextField("description", content, Field.Store.YES));
                doc.add(new StringField(keyField, filename, Field.Store.YES));
                w.addDocument(doc);
            }
        } finally {
            try { if (w != null) w.close(); } catch (Exception ignored) {}
            try { if (dir != null) dir.close(); } catch (Exception ignored) {}
        }
    }
    
    @Override
    public Map.Entry<Integer, Float> next() {
        if (it != null && it.hasNext()) return it.next();
        return null;
    }
    
    /**
     * Retourne tous les scores (pour jointure).
     * 
     * @return Map<id, score> triée par score décroissant
     */
    public Map<Integer, Float> getScores() {
        return scores;
    }
    
    @Override
    public void close() {
        // Rien à fermer
    }
}
