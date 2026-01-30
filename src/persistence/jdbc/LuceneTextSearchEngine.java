package persistence.jdbc;

import dao.search.TextSearchEngine;
import persistence.bda.TextualOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implémentation de TextSearchEngine utilisant Lucene.
 * Effectue des recherches textuelles sur le répertoire R.
 * 
 * @author Équipe Persistance
 */
public class LuceneTextSearchEngine implements TextSearchEngine {
    
    private static final String SITE_KEY_COL = "id_site";
    private static final String HOTEL_KEY_COL = "id_hotel";
    
    // Répertoire R pour les descriptions de sites
    private static final String SITE_DOCS_DIR = 
        "C:/Users/amine/eclipse-workspace/agpFinal/site_description_folder";
    
    // Répertoire R pour les descriptions d'hôtels (si nécessaire)
    
    @Override
    public List<Integer> searchSiteIdsByKeywords(String keywords) {
        TextualOperator txtOp = new TextualOperator(SITE_KEY_COL, SITE_DOCS_DIR);
        txtOp.init(keywords);
        
        Map<Integer, Float> scores = txtOp.getScores();
        
        // Extraire les IDs (déjà triés par score décroissant)
        List<Integer> ids = new ArrayList<>(scores.keySet());
        txtOp.close();
        
        return ids;
    }
    
    @Override
    public List<Integer> searchHotelIdsByKeywords(String keywords) {
        // Pour les hôtels, on utilise une recherche SQL simple (LIKE)
        // car les descriptions sont courtes (nom + beach_name)
        // Si vous voulez utiliser Lucene pour les hôtels aussi,
        // créez un répertoire hotel_description_folder avec des fichiers .txt
        
        // Pour l'instant, retourne une liste vide
        // La recherche d'hôtels se fait dans JdbcTravelDao.findHotelsByKeywords()
        return new ArrayList<>();
    }
}
