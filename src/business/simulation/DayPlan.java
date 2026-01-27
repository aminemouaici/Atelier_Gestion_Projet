package business.simulation;

import business.domain.Hotel;

/**
 * Représente le planning d'une journée
 * Peut être un jour de repos (excursion = null) ou un jour d'excursion
 */
public class DayPlan {
    
    private int dayIndex;
    private Hotel hotel;
    private Excursion excursion;
    
    // ==================== Constructeurs ====================
    
    public DayPlan() {
    }
    
    public DayPlan(int dayIndex, Hotel hotel, Excursion excursion) {
        this.dayIndex = dayIndex;
        this.hotel = hotel;
        this.excursion = excursion;
    }
    
    // ==================== Getters & Setters ====================
    
    public int getDayIndex() {
        return dayIndex;
    }
    
    public void setDayIndex(int dayIndex) {
        this.dayIndex = dayIndex;
    }
    
    public Hotel getHotel() {
        return hotel;
    }
    
    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
    
    public Excursion getExcursion() {
        return excursion;
    }
    
    public void setExcursion(Excursion excursion) {
        this.excursion = excursion;
    }
    
    // ==================== Methods ====================
    
    /**
     * Indique si c'est un jour de repos (pas d'excursion)
     * @return true si jour de repos
     */
    public boolean isRestDay() {
        // TODO: Implémenter
        return false;
    }
    
    /**
     * Calcule le coût de la journée (hôtel + excursion si présente)
     * @return coût en euros
     */
    public double getDayCost() {
        // TODO: Implémenter le calcul
        return 0.0;
    }
    
    @Override
    public String toString() {
        return "DayPlan{dayIndex=" + dayIndex + ", hotel=" + hotel.getName() +
               ", isRestDay=" + isRestDay() + "}";
    }
}
