package beans;

import business.domain.Hotel;
import business.domain.Site;
import business.service.TravelService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Managed Bean JSF pour la recherche simple de sites et hôtels
 * 
 * Ce bean est géré par Spring (pas par JSF) pour permettre
 * l'injection du TravelService.
 */
public class SearchBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // ==================== Dépendances (injectées par Spring) ====================
    
    private TravelService travelService;
    
    // ==================== Attributs du formulaire ====================
    
    /** Mots-clés saisis par l'utilisateur */
    private String keywords;
    
    /** Type de recherche : "site" ou "hotel" */
    private String searchType = "site";
    
    // ==================== Résultats ====================
    
    /** Liste des sites trouvés */
    private List<Site> siteResults = new ArrayList<>();
    
    /** Liste des hôtels trouvés */
    private List<Hotel> hotelResults = new ArrayList<>();
    
    /** Message à afficher (succès, erreur, etc.) */
    private String message;
    
    /** Indique si une recherche a été effectuée */
    private boolean searchPerformed = false;
    
    // ==================== Constructeur ====================
    
    public SearchBean() {
        // Constructeur par défaut requis pour JSF
    }
    
    // ==================== Actions JSF ====================
    
    /**
     * Action de recherche appelée par le formulaire JSF
     * @return la page de résultats (navigation JSF)
     */
    public String search() {
        // Réinitialiser les résultats
        siteResults = new ArrayList<>();
        hotelResults = new ArrayList<>();
        message = null;
        searchPerformed = true;
        
        // Validation
        if (keywords == null || keywords.trim().isEmpty()) {
            message = "Veuillez saisir des mots-clés pour la recherche.";
            return null; // Reste sur la même page
        }
        
        try {
            if ("site".equals(searchType)) {
                // Recherche de sites
                siteResults = travelService.searchSites(keywords.trim());
                
                if (siteResults.isEmpty()) {
                    message = "Aucun site trouvé pour \"" + keywords + "\".";
                } else {
                    message = siteResults.size() + " site(s) trouvé(s) pour \"" + keywords + "\".";
                }
                
            } else if ("hotel".equals(searchType)) {
                // Recherche d'hôtels
                hotelResults = travelService.searchHotels(keywords.trim());
                
                if (hotelResults.isEmpty()) {
                    message = "Aucun hôtel trouvé pour \"" + keywords + "\".";
                } else {
                    message = hotelResults.size() + " hôtel(s) trouvé(s) pour \"" + keywords + "\".";
                }
            }
            
        } catch (IllegalStateException e) {
            message = "Erreur de configuration : " + e.getMessage();
        } catch (Exception e) {
            message = "Erreur lors de la recherche : " + e.getMessage();
        }
        
        // Retourne vers la page de résultats
        return "search-results";
    }
    
    /**
     * Réinitialise le formulaire
     */
    public String reset() {
        keywords = null;
        searchType = "site";
        siteResults = new ArrayList<>();
        hotelResults = new ArrayList<>();
        message = null;
        searchPerformed = false;
        return "search";
    }
    
    // ==================== Méthodes utilitaires pour la vue ====================
    
    /**
     * Indique si on affiche les résultats de sites
     */
    public boolean isShowSiteResults() {
        return "site".equals(searchType) && searchPerformed;
    }
    
    /**
     * Indique si on affiche les résultats d'hôtels
     */
    public boolean isShowHotelResults() {
        return "hotel".equals(searchType) && searchPerformed;
    }
    
    /**
     * Indique si des résultats ont été trouvés
     */
    public boolean hasResults() {
        return !siteResults.isEmpty() || !hotelResults.isEmpty();
    }
    
    /**
     * Génère une chaîne d'étoiles pour l'affichage
     */
    public String getStarsDisplay(int stars) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stars; i++) {
            sb.append("★");
        }
        for (int i = stars; i < 5; i++) {
            sb.append("☆");
        }
        return sb.toString();
    }
    
    // ==================== Getters & Setters ====================
    
    public TravelService getTravelService() {
        return travelService;
    }
    
    public void setTravelService(TravelService travelService) {
        this.travelService = travelService;
    }
    
    public String getKeywords() {
        return keywords;
    }
    
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    
    public String getSearchType() {
        return searchType;
    }
    
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }
    
    public List<Site> getSiteResults() {
        return siteResults;
    }
    
    public void setSiteResults(List<Site> siteResults) {
        this.siteResults = siteResults;
    }
    
    public List<Hotel> getHotelResults() {
        return hotelResults;
    }
    
    public void setHotelResults(List<Hotel> hotelResults) {
        this.hotelResults = hotelResults;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isSearchPerformed() {
        return searchPerformed;
    }
    
    public void setSearchPerformed(boolean searchPerformed) {
        this.searchPerformed = searchPerformed;
    }
}
