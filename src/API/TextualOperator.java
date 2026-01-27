package API;

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

public class TextualOperator implements Operator<Map.Entry<Integer, Float>> {

    private final String keyField;
    private final String docsDir;
    private final Path indexPath = FileSystems.getDefault().getPath("/tmp/lucene_index");

    private LuceneSearch lucene;
    private Map<Integer, Float> scores = new LinkedHashMap<>();
    private Iterator<Map.Entry<Integer, Float>> it;

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
            scores = new LinkedHashMap<Integer, Float>();
            it = scores.entrySet().iterator();
        }
    }

    private void buildIndex() throws Exception {
        // Rebuild simple : on recrée à chaque init
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
                String filename = f.getName().replace(".txt", "");

                // ✅ Java 8 compatible (remplace Files.readString)
                String content = new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);

                Document doc = new Document();
                doc.add(new TextField("description", content, Field.Store.YES));
                doc.add(new StringField(keyField, filename, Field.Store.YES));
                w.addDocument(doc);
            }
        } finally {
            // fermeture propre (Java 8)
            try { if (w != null) w.close(); } catch (Exception ignored) {}
            try { if (dir != null) dir.close(); } catch (Exception ignored) {}
        }
    }

    @Override
    public Map.Entry<Integer, Float> next() {
        if (it != null && it.hasNext()) return it.next();
        return null;
    }

    public Map<Integer, Float> getScores() {
        return scores;
    }

    @Override
    public void close() {
        // rien
    }
}
