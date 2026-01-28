package dao;

import business.domain.Hotel;
import business.domain.Site;

import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour l'accès aux données Sites et Hotels.
 * Implémentation : JdbcTravelDao (package persistence.jdbc)
 * 
 * @author Équipe Persistance
 */
public interface TravelDao {

    // ==================== SITES ====================
    
    /**
     * Retourne tous les sites disponibles.
     * Requête SQL pure.
     * 
     * @return Liste complète des sites
     */
    List<Site> findAllSites();
    
    /**
     * Trouve un site par son ID.
     * Requête SQL pure.
     * 
     * @param siteId ID du site
     * @return Optional contenant le site ou vide
     */
    Optional<Site> findSiteById(int siteId);
    
    /**
     * Recherche de sites par mots-clés (utilise Lucene).
     * ⚠️ REQUÊTE MIXTE SQL-Text conforme au Plan 1 du cahier des charges.
     * 
     * Exécution :
     * 1. TextOperator : Recherche Lucene sur répertoire R
     * 2. SqlOperator : SELECT * FROM Site
     * 3. JoinOperator : Jointure sur id_site
     * 4. Tri par score Lucene décroissant
     * 
     * @param keywords Mots-clés de recherche (ex: "musée culture")
     * @return Liste des sites triés par pertinence (score Lucene décroissant)
     */
    List<Site> findSitesByKeywords(String keywords);
    
    /**
     * Recherche de sites par type.
     * Requête SQL pure.
     * 
     * @param siteType "HISTORICAL" ou "ACTIVITY"
     * @return Liste des sites du type demandé
     */
    List<Site> findSitesByType(String siteType);
    
    /**
     * Recherche de sites par fourchette de prix.
     * Requête SQL pure.
     * 
     * @param min Prix minimum
     * @param max Prix maximum
     * @return Liste des sites dans la fourchette
     */
    List<Site> findSitesByPriceRange(double min, double max);
    
    // ==================== HOTELS ====================
    
    /**
     * Retourne tous les hôtels disponibles.
     * Requête SQL pure.
     * 
     * @return Liste complète des hôtels
     */
    List<Hotel> findAllHotels();
    
    /**
     * Trouve un hôtel par son ID.
     * Requête SQL pure.
     * 
     * @param hotelId ID de l'hôtel
     * @return Optional contenant l'hôtel ou vide
     */
    Optional<Hotel> findHotelById(int hotelId);
    
    /**
     * Recherche d'hôtels par nombre d'étoiles minimum.
     * Requête SQL pure.
     * 
     * @param minStars Nombre minimum d'étoiles (1-5)
     * @return Liste des hôtels avec au moins minStars étoiles
     */
    List<Hotel> findHotelsByMinStars(int minStars);
    
    /**
     * Recherche d'hôtels par fourchette de prix.
     * Requête SQL pure.
     * 
     * @param min Prix minimum par nuit
     * @param max Prix maximum par nuit
     * @return Liste des hôtels dans la fourchette
     */
    List<Hotel> findHotelsByPriceRange(double min, double max);
    
    /**
     * Recherche d'hôtels par mots-clés.
     * Requête SQL pure (LIKE sur nom et beach_name).
     * 
     * @param keywords Mots-clés de recherche
     * @return Liste des hôtels correspondants
     */
    List<Hotel> findHotelsByKeywords(String keywords);
}
