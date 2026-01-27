package business.simulation;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une offre de séjour complète
 * Contient le planning jour par jour, le prix total et le score de confort
 * OBJET CALCULÉ - NON PERSISTÉ EN BASE
 */
public class Offer {
    
    private List<DayPlan> days;
    private double totalPrice;
    private int comfortScore;
    
    // ==================== Constructeurs ====================
    
    public Offer() {
        this.days = new ArrayList<>();
    }
    
    public Offer(List<DayPlan> days) {
        this.days = days;
    }
    
    public Offer(List<DayPlan> days, double totalPrice, int comfortScore) {
        this.days = days;
        this.totalPrice = totalPrice;
        this.comfortScore = comfortScore;
    }
    
    // ==================== Getters & Setters ====================
    
    public List<DayPlan> getDays() {
        return days;
    }
    
    public void setDays(List<DayPlan> days) {
        this.days = days;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public int getComfortScore() {
        return comfortScore;
    }
    
    public void setComfortScore(int comfortScore) {
        this.comfortScore = comfortScore;
    }
    
    // ==================== Methods ====================
    
    /**
     * Calcule le prix total de l'offre
     * @return prix en euros
     */
    public double calculatePrice() {
        // TODO: Implémenter le calcul
        return 0.0;
    }
    
    /**
     * Retourne le nombre de jours du séjour
     * @return nombre de jours
     */
    public int getNbDays() {
        return days.size();
    }
    
    /**
     * Retourne le nombre de jours de repos
     * @return nombre de jours de repos
     */
    public int getRestDaysCount() {
        // TODO: Implémenter
        return 0;
    }
    
    @Override
    public String toString() {
        return "Offer{nbDays=" + days.size() + ", totalPrice=" + totalPrice +
               ", comfortScore=" + comfortScore + "}";
    }
}
