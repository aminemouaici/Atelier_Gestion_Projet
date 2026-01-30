package persistence.bda;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    
    // ✅ MODIFICATION 1 : Index PERSISTANT sur disque (pas en /tmp)
    private final Path indexPath = Paths.get("C:/Users/amine/eclipse-workspace/agpFinal/lucene_index");
    
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
            // ✅ MODIFICATION 2 : Vérifier si l'index existe, sinon le créer
            if (!indexExists()) {
                System.out.println("⏳ Index Lucene n'existe pas. Création en cours...");
                buildIndex();
                System.out.println("✅ Index Lucene créé : " + indexPath);
            } else {
                System.out.println("✅ Index Lucene existant trouvé : " + indexPath);
            }
            
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
     * ✅ AJOUT : Vérifie si l'index Lucene existe déjà sur disque.
     * 
     * @return true si l'index existe et est valide, false sinon
     */
    private boolean indexExists() {
        try {
            // Vérifier si le dossier existe
            if (!Files.exists(indexPath)) {
                return false;
            }
            
            // Vérifier si l'index est valide
            Directory dir = FSDirectory.open(indexPath);
            boolean exists = DirectoryReader.indexExists(dir);
            dir.close();
            
            return exists;
        } catch (Exception e) {
            return false;
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
    
// ========== MÉTHODES UTILITAIRES ==========
    
    /**
     * Ajoute un texte t à la ligne de clé c de la table T.
     * CONFORME EXIGENCE 2 : "Ajouter un texte t à la ligne de clé c de la table T,
     *                        en créant le fichier associé dans R"
     * 
     * Le fichier créé aura pour nom : c.txt (ex: 1.txt, 2.txt, 3.txt)
     * Méthode utilitaire statique utilisable depuis n'importe où.
     * 
     * @param docsDir Répertoire R où créer le fichier
     * @param key Clé c de la ligne (ex: 1, 2, 3)
     * @param text Texte t à associer à cette ligne
     * @throws Exception Si erreur lors de la création du fichier
     */
    public static void addTextDocument(String docsDir, int key, String text) throws Exception {
        // Créer le répertoire s'il n'existe pas
        File dir = new File(docsDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // Créer le fichier c.txt
        File file = new File(dir, key + ".txt");
        
        // Écrire le texte en UTF-8 (compatible Java 8)
        Files.write(file.toPath(), text.getBytes(StandardCharsets.UTF_8));
        
        System.out.println("✅ Fichier créé : " + file.getAbsolutePath());
    }
    
    /**
     * Ajoute plusieurs textes en une seule fois.
     * Utile pour initialiser le répertoire R avec plusieurs documents.
     * 
     * @param docsDir Répertoire R où créer les fichiers
     * @param keyTextPairs Map<clé, texte> des documents à créer
     * @throws Exception Si erreur lors de la création des fichiers
     */
    public static void addTextDocuments(String docsDir, Map<Integer, String> keyTextPairs) throws Exception {
        for (Map.Entry<Integer, String> entry : keyTextPairs.entrySet()) {
            addTextDocument(docsDir, entry.getKey(), entry.getValue());
        }
    }
    
    /**
     * Vérifie si un fichier texte existe pour une clé donnée.
     * 
     * @param docsDir Répertoire R
     * @param key Clé à vérifier
     * @return true si le fichier c.txt existe, false sinon
     */
    public static boolean textDocumentExists(String docsDir, int key) {
        File file = new File(docsDir, key + ".txt");
        return file.exists();
    }
    
    /**
     * Supprime un fichier texte du répertoire R.
     * 
     * @param docsDir Répertoire R
     * @param key Clé du document à supprimer
     * @return true si supprimé avec succès, false sinon
     */
    public static boolean removeTextDocument(String docsDir, int key) {
        File file = new File(docsDir, key + ".txt");
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("✅ Fichier supprimé : " + file.getAbsolutePath());
            }
            return deleted;
        }
        return false;
    }
    
    /**
     * Compte le nombre de fichiers texte dans le répertoire R.
     * 
     * @param docsDir Répertoire R
     * @return Nombre de fichiers .txt
     */
    public static int countTextDocuments(String docsDir) {
        File dir = new File(docsDir);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        return (files != null) ? files.length : 0;
    }
}