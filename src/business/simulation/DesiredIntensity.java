package business.simulation;

/**
 * Enumération des niveaux d'intensité souhaités pour le séjour
 * Enum enrichi avec fréquence de repos et nombre max de sites par jour
 */
public enum DesiredIntensity {
    
    LOW(2, 2),      // Repos 1 jour sur 2, max 2 sites/jour
    MEDIUM(3, 2),   // Repos 1 jour sur 3, max 2 sites/jour
    HIGH(0, 3);     // Pas de repos, max 3 sites/jour
    
    private final int restFrequency;
    private final int maxSitesPerDay;
    
    // ==================== Constructeur ====================
    
    DesiredIntensity(int restFrequency, int maxSitesPerDay) {
        this.restFrequency = restFrequency;
        this.maxSitesPerDay = maxSitesPerDay;
    }
    
    // ==================== Getters ====================
    
    /**
     * Retourne la fréquence de repos (0 = jamais, 2 = 1 jour sur 2, etc.)
     */
    public int getRestFrequency() {
        return restFrequency;
    }
    
    /**
     * Retourne le nombre maximum de sites par excursion
     */
    public int getMaxSitesPerDay() {
        return maxSitesPerDay;
    }
}
