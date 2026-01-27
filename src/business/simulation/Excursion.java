package business.simulation;

import business.domain.Hotel;
import business.domain.Site;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une excursion (journée de visite)
 * Contient une liste de sites à visiter et les trajets associés
 * Maximum 3 sites par excursion (contrainte cahier des charges)
 */
public class Excursion {
    
    private static final int MAX_SITES = 3;
    
    private List<Site> sites;
    private Hotel hotel;
    private List<TravelLeg> legs;
    private double cost;
    
    // ==================== Constructeurs ====================
    
    public Excursion() {
        this.sites = new ArrayList<>();
        this.legs = new ArrayList<>();
    }
    
    public Excursion(List<Site> sites, Hotel hotel, List<TravelLeg> legs) {
        this.sites = sites;
        this.hotel = hotel;
        this.legs = legs;
    }
    
    // ==================== Getters & Setters ====================
    
    public List<Site> getSites() {
        return sites;
    }
    
    public void setSites(List<Site> sites) {
        this.sites = sites;
    }
    
    public Hotel getHotel() {
        return hotel;
    }
    
    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
    
    public List<TravelLeg> getLegs() {
        return legs;
    }
    
    public void setLegs(List<TravelLeg> legs) {
        this.legs = legs;
    }
    
    public double getCost() {
        return cost;
    }
    
    public void setCost(double cost) {
        this.cost = cost;
    }
    
    // ==================== Methods ====================
    
    /**
     * Calcule le coût total de l'excursion
     * (entrées des sites + coûts des trajets)
     * @return coût total en euros
     */
    public double calculateCost() {
        // TODO: Implémenter le calcul
        return 0.0;
    }
    
    /**
     * Vérifie que l'excursion est valide (max 3 sites)
     * @return true si valide
     */
    public boolean isValid() {
        // TODO: Implémenter la validation
        return false;
    }
    
    @Override
    public String toString() {
        return "Excursion{sites=" + sites.size() + ", hotel=" + hotel.getName() +
               ", legs=" + legs.size() + ", cost=" + cost + "}";
    }
}
