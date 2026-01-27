package business.simulation;

import business.domain.Position;
import business.domain.TransportMode;

/**
 * Représente un trajet entre deux positions
 * Calcule automatiquement la distance, le coût et la durée
 */
public class TravelLeg {
    
    private Position from;
    private Position to;
    private TransportMode mode;
    private double distanceKm;
    private double cost;
    private int durationMin;
    
    // ==================== Constructeurs ====================
    
    public TravelLeg() {
    }
    
    public TravelLeg(Position from, Position to, TransportMode mode) {
        this.from = from;
        this.to = to;
        this.mode = mode;
    }
    
    // ==================== Getters & Setters ====================
    
    public Position getFrom() {
        return from;
    }
    
    public void setFrom(Position from) {
        this.from = from;
    }
    
    public Position getTo() {
        return to;
    }
    
    public void setTo(Position to) {
        this.to = to;
    }
    
    public TransportMode getMode() {
        return mode;
    }
    
    public void setMode(TransportMode mode) {
        this.mode = mode;
    }
    
    public double getDistanceKm() {
        return distanceKm;
    }
    
    public double getCost() {
        return cost;
    }
    
    public int getDurationMin() {
        return durationMin;
    }
    
    // ==================== Methods ====================
    
    /**
     * Calcule la distance, le coût et la durée du trajet
     */
    public void calculate() {
        // TODO: Implémenter le calcul
    }
    
    @Override
    public String toString() {
        return "TravelLeg{from=" + from + ", to=" + to + ", mode=" + mode +
               ", distanceKm=" + distanceKm + ", cost=" + cost + ", durationMin=" + durationMin + "}";
    }
}
