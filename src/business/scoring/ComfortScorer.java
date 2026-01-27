package business.scoring;

import business.domain.Hotel;
import business.simulation.DayPlan;
import business.simulation.Offer;
import business.simulation.TravelLeg;

import java.util.List;

/**
 * Calculateur de score de confort pour les offres
 * Score de 0 à 100
 */
public class ComfortScorer {
    
    // ==================== Constructeurs ====================
    
    public ComfortScorer() {
    }
    
    // ==================== Methods ====================
    
    /**
     * Calcule le score de confort d'une offre
     * @param offer l'offre à évaluer
     * @return score de 0 à 100
     */
    public int calculate(Offer offer) {
        // TODO: Implémenter le calcul
        return 0;
    }
    
    /**
     * Calcule le score lié aux transports
     */
    private int scoreTransport(List<TravelLeg> legs) {
        // TODO: Implémenter
        return 0;
    }
    
    /**
     * Calcule le score lié aux jours de repos
     */
    private int scoreRestDays(List<DayPlan> days) {
        // TODO: Implémenter
        return 0;
    }
    
    /**
     * Calcule le score lié à l'hôtel
     */
    private int scoreHotel(Hotel hotel) {
        // TODO: Implémenter
        return 0;
    }
}
