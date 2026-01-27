package business.scoring;

import business.domain.Hotel;
import business.simulation.DayPlan;
import business.simulation.Excursion;
import business.simulation.Offer;
import business.simulation.TravelLeg;

/**
 * Calculateur de prix pour les offres
 * Responsable de calculer le coût total d'une offre
 */
public class PriceCalculator {
    
    // ==================== Constructeurs ====================
    
    public PriceCalculator() {
    }
    
    // ==================== Methods ====================
    
    /**
     * Calcule le prix total d'une offre
     * @param offer l'offre à calculer
     * @return prix total en euros
     */
    public double calculateTotal(Offer offer) {
        // TODO: Implémenter le calcul
        return 0.0;
    }
    
    /**
     * Calcule le coût d'une excursion
     * @param excursion l'excursion à calculer
     * @return coût en euros
     */
    public double calculateExcursionCost(Excursion excursion) {
        // TODO: Implémenter le calcul
        return 0.0;
    }
    
    /**
     * Calcule le coût de l'hôtel pour un nombre de nuits
     * @param hotel l'hôtel
     * @param nights nombre de nuits
     * @return coût en euros
     */
    public double calculateHotelCost(Hotel hotel, int nights) {
        // TODO: Implémenter le calcul
        return 0.0;
    }
    
    /**
     * Calcule le coût d'un trajet
     * @param leg le trajet
     * @return coût en euros
     */
    public double calculateTransportCost(TravelLeg leg) {
        // TODO: Implémenter le calcul
        return 0.0;
    }
}
