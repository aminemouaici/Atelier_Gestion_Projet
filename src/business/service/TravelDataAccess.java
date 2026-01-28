package business.service;

import business.domain.Hotel;
import business.domain.Site;
import dao.TravelDao;

import java.util.List;

/**
 * Interface d'accès aux données
 * Sera implémentée par la couche persistance
 * Permet le découplage entre la couche métier et la couche DAO
 * 
 * CONTRAT pour l'équipe persistance :
 * - Vous DEVEZ implémenter cette interface
 * - Les méthodes de recherche textuelle utiliseront Lucene
 * - Les méthodes de recherche structurelle utiliseront SQL
 */
public interface TravelDataAccess extends TravelDao {
    
    // ==================== Recherche de Sites ====================
    
    /**
     * Recherche des sites par mots-clés (recherche textuelle)
     * Utilisera Lucene côté persistance
     * 
     * @param keywords les mots-clés de recherche
     * @return liste des sites correspondants (jamais null)
     */
    List<Site> findSitesByKeywords(String keywords);
    
    /**
     * Recherche des sites par fourchette de budget
     * 
     * @param min prix minimum
     * @param max prix maximum
     * @return liste des sites dans la fourchette
     */
    List<Site> findSitesByBudget(double min, double max);
    
    /**
     * Retourne tous les sites disponibles
     * 
     * @return liste de tous les sites
     */
    List<Site> getAllSites();
    
    // ==================== Recherche d'Hôtels ====================
    
    /**
     * Recherche des hôtels par mots-clés (recherche textuelle)
     * Utilisera Lucene côté persistance
     * 
     * @param keywords les mots-clés de recherche (ex: "plage", "luxe")
     * @return liste des hôtels correspondants (jamais null)
     */
    List<Hotel> findHotelsByKeywords(String keywords);
    
    /**
     * Recherche des hôtels par nombre d'étoiles minimum
     * Utilisera SQL côté persistance
     * 
     * @param minStars nombre minimum d'étoiles (1 à 5)
     * @return liste des hôtels correspondants
     */
    List<Hotel> findHotelsByStars(int minStars);
    
    /**
     * Retourne tous les hôtels disponibles
     * 
     * @return liste de tous les hôtels
     */
    List<Hotel> getAllHotels();
}
