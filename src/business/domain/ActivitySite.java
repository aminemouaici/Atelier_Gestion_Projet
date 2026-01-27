package business.domain;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Représente un site touristique de type activité/loisir
 * (plongée, surf, randonnée, etc.)
 */
public class ActivitySite extends Site {
    
    // ==================== Constructeurs ====================
    
    public ActivitySite() {
        super();
    }
    
    public ActivitySite(int id, String name, double price, Position position,
                        String description, LocalTime startTime, LocalTime endTime, Duration duration) {
        super(id, name, price, position, description, startTime, endTime, duration);
    }
    
    // ==================== Methods ====================
    
    @Override
    public boolean isHistorical() {
        return false;
    }
    
    @Override
    public String toString() {
        return "ActivitySite{" + super.toString() + "}";
    }
}
