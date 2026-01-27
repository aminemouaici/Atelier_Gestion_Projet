package business.planning;

import business.domain.Hotel;
import business.domain.Site;
import business.scoring.ComfortScorer;
import business.scoring.PriceCalculator;
import business.simulation.Offer;
import business.simulation.OfferRequest;

import java.util.List;

/**
 * Orchestrateur de la génération d'offres
 * Utilise la Factory pour obtenir la bonne stratégie
 * Utilise PriceCalculator et ComfortScorer pour finaliser les offres
 */
public class OfferGenerator {
    
    private StrategyFactory factory;
    private PriceCalculator priceCalculator;
    private ComfortScorer comfortScorer;
    
    // ==================== Constructeurs ====================
    
    public OfferGenerator() {
        this.factory = new StrategyFactory();
        this.priceCalculator = new PriceCalculator();
        this.comfortScorer = new ComfortScorer();
    }
    
    public OfferGenerator(StrategyFactory factory, PriceCalculator priceCalculator, ComfortScorer comfortScorer) {
        this.factory = factory;
        this.priceCalculator = priceCalculator;
        this.comfortScorer = comfortScorer;
    }
    
    // ==================== Getters & Setters ====================
    
    public StrategyFactory getFactory() {
        return factory;
    }
    
    public void setFactory(StrategyFactory factory) {
        this.factory = factory;
    }
    
    public PriceCalculator getPriceCalculator() {
        return priceCalculator;
    }
    
    public void setPriceCalculator(PriceCalculator priceCalculator) {
        this.priceCalculator = priceCalculator;
    }
    
    public ComfortScorer getComfortScorer() {
        return comfortScorer;
    }
    
    public void setComfortScorer(ComfortScorer comfortScorer) {
        this.comfortScorer = comfortScorer;
    }
    
    // ==================== Methods ====================
    
    /**
     * Génère une liste d'offres selon les critères
     * @param req les critères de recherche
     * @param sites la liste des sites disponibles
     * @param hotels la liste des hôtels disponibles
     * @return liste d'offres complètes (avec prix et score)
     */
    public List<Offer> generate(OfferRequest req, List<Site> sites, List<Hotel> hotels) {
        // TODO: Implémenter la génération
        return null;
    }
    
    /**
     * Sélectionne le meilleur hôtel pour les sites donnés
     */
    private Hotel selectBestHotel(List<Site> sites, int starRating) {
        // TODO: Implémenter la sélection
        return null;
    }
    
    /**
     * Sélectionne le site le plus proche de l'hôtel
     */
    private Site selectNearestSite(Hotel hotel, List<Site> sites) {
        // TODO: Implémenter la sélection
        return null;
    }
}
