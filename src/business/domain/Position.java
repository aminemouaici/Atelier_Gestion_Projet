package business.domain;

/**
 * Représente une position géographique (coordonnées GPS)
 * Utilisée pour calculer les distances entre structures
 */
public class Position {
    
    private double latitude;
    private double longitude;
    
    // ==================== Constructeurs ====================
    
    public Position() {
    }
    
    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    // ==================== Getters & Setters ====================
    
    public double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    
    // ==================== Methods ====================
    
    /**
     * Calcule la distance en kilomètres vers une autre position
     * Utilise la formule de Haversine
     * @param other la position cible
     * @return distance en km
     */
    public double distanceTo(Position other) {
        // TODO: Implémenter la formule de Haversine
        return 0.0;
    }
    
    @Override
    public String toString() {
        return "Position{latitude=" + latitude + ", longitude=" + longitude + "}";
    }
}
