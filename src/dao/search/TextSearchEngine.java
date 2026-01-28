package dao.search;

import java.util.List;

/**
 * Interface pour la recherche textuelle (Lucene).
 * Retourne uniquement les IDs des résultats triés par pertinence.
 * 
 * Implémentation : LuceneTextSearchEngine (package persistence.jdbc)
 * 
 * @author Équipe Persistance
 */
public interface TextSearchEngine {

    /**
     * Recherche des IDs de sites par mots-clés.
     * Utilise Lucene sur le répertoire R (fichiers texte).
     * 
     * @param keywords Mots-clés de recherche (ex: "plongée corail")
     * @return Liste des IDs de sites triés par pertinence (score Lucene décroissant)
     */
    List<Integer> searchSiteIdsByKeywords(String keywords);

    /**
     * Recherche des IDs d'hôtels par mots-clés.
     * Utilise une recherche simple SQL (LIKE sur nom/description).
     * 
     * @param keywords Mots-clés de recherche (ex: "plage luxe")
     * @return Liste des IDs d'hôtels triés par pertinence
     */
    List<Integer> searchHotelIdsByKeywords(String keywords);
}
