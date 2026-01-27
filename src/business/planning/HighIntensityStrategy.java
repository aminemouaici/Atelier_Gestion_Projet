package business.planning;

import business.domain.Hotel;
import business.domain.Site;
import business.simulation.Offer;
import business.simulation.OfferRequest;

import java.util.List;

/**
 * Stratégie de génération d'offres pour un séjour INTENSIF (HIGH)
 * - Pas de jour de repos
 * - Maximum 3 sites par excursion
 */
public class HighIntensityStrategy implements OfferStrategy {
    
    private static final int REST_FREQUENCY = 0;
    private static final int MAX_SITES_PER_DAY = 3;
    
    // ==================== Constructeurs ====================
    
    public HighIntensityStrategy() {
    }
    
    // ==================== Methods ====================
    
    @Override
    public List<Offer> buildOffers(OfferRequest req, List<Site> sites, List<Hotel> hotels) {
        // TODO: Implémenter la génération d'offres intensives
        return null;
    }
    
    /**
     * Sélectionne le meilleur hôtel selon les critères
     */
    protected Hotel selectBestHotel(List<Site> sites, List<Hotel> hotels, int minStars) {
        // TODO: Implémenter la sélection
        return null;
    }
    
    /**
     * Sélectionne le site le plus proche de l'hôtel
     */
    protected Site selectNearestSite(Hotel hotel, List<Site> remainingSites) {
        // TODO: Implémenter la sélection
        return null;
    }
    
    /**
     * Détermine si le jour donné est un jour de repos
     */
    protected boolean isRestDay(int dayIndex) {
        // Jamais de repos en mode HIGH
        return false;
    }
}
