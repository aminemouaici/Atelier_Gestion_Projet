package business.planning;

import business.simulation.DesiredIntensity;

/**
 * Factory pour créer la bonne stratégie selon l'intensité demandée
 * Pattern Factory
 */
public class StrategyFactory {
    
    // ==================== Constructeurs ====================
    
    public StrategyFactory() {
    }
    
    // ==================== Methods ====================
    
    /**
     * Retourne la stratégie appropriée selon l'intensité
     * @param intensity niveau d'intensité souhaité
     * @return la stratégie correspondante
     */
    public OfferStrategy getStrategy(DesiredIntensity intensity) {
        // TODO: Implémenter le switch
        return null;
    }
}
