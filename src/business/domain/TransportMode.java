package business.domain;

/**
 * Enumération des modes de transport disponibles
 * Enum enrichi avec score de confort et prix au kilomètre
 */
public enum TransportMode {
    
    FOOT(2, 0.0, 5),
    BUS(5, 0.5, 40),
    BOAT(10, 2.0, 30);
    
    private final int comfortScore;
    private final double pricePerKm;
    private final int speedKmH;
    
    // ==================== Constructeur ====================
    
    TransportMode(int comfortScore, double pricePerKm, int speedKmH) {
        this.comfortScore = comfortScore;
        this.pricePerKm = pricePerKm;
        this.speedKmH = speedKmH;
    }
    
    // ==================== Getters ====================
    
    public int getComfortScore() {
        return comfortScore;
    }
    
    public double getPricePerKm() {
        return pricePerKm;
    }
    
    public int getSpeedKmH() {
        return speedKmH;
    }
}
