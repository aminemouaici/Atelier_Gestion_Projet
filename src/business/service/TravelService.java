package business.service;

import business.domain.Hotel;
import business.domain.Site;
import business.planning.OfferGenerator;
import business.simulation.Offer;
import business.simulation.OfferRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Service principal de la couche métier (Façade)
 * Point d'entrée unique pour les contrôleurs MVC (Beans JSF)
 * 
 * Cette classe ne fait PAS d'accès direct à la base de données.
 * Elle délègue tout à l'interface TravelDataAccess.
 */
public class TravelService {
    
    // ==================== Attributs ====================
    
    private OfferGenerator generator;
    private TravelDataAccess dataAccess;
    
    // ==================== Constructeurs ====================
    
    public TravelService() {
        this.generator = new OfferGenerator();
    }
    
    public TravelService(TravelDataAccess dataAccess) {
        this.generator = new OfferGenerator();
        this.dataAccess = dataAccess;
    }
    
    public TravelService(OfferGenerator generator, TravelDataAccess dataAccess) {
        this.generator = generator;
        this.dataAccess = dataAccess;
    }
    
    // ==================== Getters & Setters ====================
    
    public OfferGenerator getGenerator() {
        return generator;
    }
    
    public void setGenerator(OfferGenerator generator) {
        this.generator = generator;
    }
    
    public TravelDataAccess getDataAccess() {
        return dataAccess;
    }
    
    public void setDataAccess(TravelDataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }
    
    // ==================== Méthodes de recherche ====================
    
    /**
     * Recherche des sites par mots-clés
     * 
     * @param keywords les mots-clés de recherche (ex: "plongée", "musée", "randonnée")
     * @return liste des sites correspondants, liste vide si aucun résultat
     * @throws IllegalStateException si dataAccess n'est pas configuré
     * @throws IllegalArgumentException si keywords est null ou vide
     */
    public List<Site> searchSites(String keywords) {
        // 1. Vérifier que dataAccess est configuré
        if (dataAccess == null) {
            throw new IllegalStateException("TravelDataAccess n'est pas configuré. Vérifiez l'injection Spring.");
        }
        
        // 2. Vérifier que keywords n'est pas null ou vide
        if (keywords == null || keywords.trim().isEmpty()) {
            throw new IllegalArgumentException("Les mots-clés de recherche ne peuvent pas être vides.");
        }
        
        // 3. Nettoyer les mots-clés (trim)
        String cleanedKeywords = keywords.trim();
        
        // 4. Déléguer la recherche à la couche DAO
        List<Site> results = dataAccess.findSitesByKeywords(cleanedKeywords);
        
        // 5. Retourner les résultats (jamais null)
        return results != null ? results : new ArrayList<>();
    }
    
    /**
     * Recherche des hôtels par mots-clés
     * 
     * @param keywords les mots-clés de recherche (ex: "plage", "luxe", "piscine")
     * @return liste des hôtels correspondants, liste vide si aucun résultat
     * @throws IllegalStateException si dataAccess n'est pas configuré
     * @throws IllegalArgumentException si keywords est null ou vide
     */
    public List<Hotel> searchHotels(String keywords) {
        // 1. Vérifier que dataAccess est configuré
        if (dataAccess == null) {
            throw new IllegalStateException("TravelDataAccess n'est pas configuré. Vérifiez l'injection Spring.");
        }
        
        // 2. Vérifier que keywords n'est pas null ou vide
        if (keywords == null || keywords.trim().isEmpty()) {
            throw new IllegalArgumentException("Les mots-clés de recherche ne peuvent pas être vides.");
        }
        
        // 3. Nettoyer les mots-clés (trim)
        String cleanedKeywords = keywords.trim();
        
        // 4. Déléguer la recherche à la couche DAO
        List<Hotel> results = dataAccess.findHotelsByKeywords(cleanedKeywords);
        
        // 5. Retourner les résultats (jamais null)
        return results != null ? results : new ArrayList<>();
    }
    
    /**
     * Recherche des hôtels par nombre d'étoiles minimum
     * 
     * @param minStars nombre minimum d'étoiles (1 à 5)
     * @return liste des hôtels correspondants
     * @throws IllegalStateException si dataAccess n'est pas configuré
     * @throws IllegalArgumentException si minStars n'est pas entre 1 et 5
     */
    public List<Hotel> searchHotelsByStars(int minStars) {
        // 1. Vérifier que dataAccess est configuré
        if (dataAccess == null) {
            throw new IllegalStateException("TravelDataAccess n'est pas configuré. Vérifiez l'injection Spring.");
        }
        
        // 2. Vérifier que minStars est valide (entre 1 et 5)
        if (minStars < 1 || minStars > 5) {
            throw new IllegalArgumentException("Le nombre d'étoiles doit être entre 1 et 5. Reçu: " + minStars);
        }
        
        // 3. Déléguer la recherche à la couche DAO
        List<Hotel> results = dataAccess.findHotelsByStars(minStars);
        
        // 4. Retourner les résultats (jamais null)
        return results != null ? results : new ArrayList<>();
    }
    
    /**
     * Retourne tous les sites disponibles
     * 
     * @return liste de tous les sites
     */
    public List<Site> getAllSites() {
        if (dataAccess == null) {
            throw new IllegalStateException("TravelDataAccess n'est pas configuré.");
        }
        
        List<Site> results = dataAccess.getAllSites();
        return results != null ? results : new ArrayList<>();
    }
    
    /**
     * Retourne tous les hôtels disponibles
     * 
     * @return liste de tous les hôtels
     */
    public List<Hotel> getAllHotels() {
        if (dataAccess == null) {
            throw new IllegalStateException("TravelDataAccess n'est pas configuré.");
        }
        
        List<Hotel> results = dataAccess.getAllHotels();
        return results != null ? results : new ArrayList<>();
    }
    
    // ==================== Méthodes de génération d'offres ====================
    
    /**
     * Génère une liste d'offres selon les critères de la requête
     * (sera implémenté plus tard)
     * 
     * @param req les critères de recherche
     * @return liste d'offres générées
     */
    public List<Offer> buildOffers(OfferRequest req) {
        // TODO: Implémenter dans la prochaine étape
        return null;
    }
}
