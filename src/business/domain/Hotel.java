package business.domain;

/**
 * Représente un hôtel avec sa classification et sa plage associée
 */
public class Hotel extends Structure {
    
    private int starRating;
    private String beachName;
    
    // ==================== Constructeurs ====================
    
    public Hotel() {
        super();
    }
    
    public Hotel(int id, String name, double price, Position position,
                 int starRating, String beachName) {
        super(id, name, price, position);
        this.starRating = starRating;
        this.beachName = beachName;
    }
    
    // ==================== Getters & Setters ====================
    
    public int getStarRating() {
        return starRating;
    }
    
    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }
    
    public String getBeachName() {
        return beachName;
    }
    
    public void setBeachName(String beachName) {
        this.beachName = beachName;
    }
    
    // ==================== Methods ====================
    
    @Override
    public String toString() {
        return "Hotel{" + super.toString() + ", starRating=" + starRating + ", beachName='" + beachName + "'}";
    }
}
