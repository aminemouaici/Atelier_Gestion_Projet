package business.domain;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Représente un site touristique de type historique
 * (monuments, musées, lieux patrimoniaux, etc.)
 */
public class HistoricalSite extends Site {
    
    // ==================== Constructeurs ====================
    
    public HistoricalSite() {
        super();
    }
    
    public HistoricalSite(int id, String name, double price, Position position,
                          String description, LocalTime startTime, LocalTime endTime, Duration duration) {
        super(id, name, price, position, description, startTime, endTime, duration);
    }
    
    // ==================== Methods ====================
    
    @Override
    public boolean isHistorical() {
        return true;
    }
    
    @Override
    public String toString() {
        return "HistoricalSite{" + super.toString() + "}";
    }
}
