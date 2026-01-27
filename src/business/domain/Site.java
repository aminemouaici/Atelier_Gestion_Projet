package business.domain;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Classe abstraite représentant un site touristique
 * Peut être un site historique ou un site d'activité
 */
public abstract class Site extends Structure {
    
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private Duration duration;
    
    // ==================== Constructeurs ====================
    
    public Site() {
        super();
    }
    
    public Site(int id, String name, double price, Position position,
                String description, LocalTime startTime, LocalTime endTime, Duration duration) {
        super(id, name, price, position);
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
    }
    
    // ==================== Getters & Setters ====================
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    
    public Duration getDuration() {
        return duration;
    }
    
    public void setDuration(Duration duration) {
        this.duration = duration;
    }
    
    // ==================== Abstract Methods ====================
    
    /**
     * Indique si le site est de type historique
     * @return true si historique, false si activité
     */
    public abstract boolean isHistorical();
    
    // ==================== Methods ====================
    
    @Override
    public String toString() {
        return super.toString() + ", description='" + description + "'";
    }
}
