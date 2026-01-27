package business.simulation;

/**
 * Représente une requête de génération d'offres
 * Encapsule tous les critères de recherche de l'utilisateur
 */
public class OfferRequest {
    
    private String keywords;
    private double budget;
    private int nbDays;
    private DesiredIntensity intensity;
    private int starRating;
    private String typeActivity;
    
    // ==================== Constructeurs ====================
    
    public OfferRequest() {
    }
    
    public OfferRequest(String keywords, double budget, int nbDays,
                        DesiredIntensity intensity, int starRating, String typeActivity) {
        this.keywords = keywords;
        this.budget = budget;
        this.nbDays = nbDays;
        this.intensity = intensity;
        this.starRating = starRating;
        this.typeActivity = typeActivity;
    }
    
    // ==================== Getters & Setters ====================
    
    public String getKeywords() {
        return keywords;
    }
    
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    
    public double getBudget() {
        return budget;
    }
    
    public void setBudget(double budget) {
        this.budget = budget;
    }
    
    public int getNbDays() {
        return nbDays;
    }
    
    public void setNbDays(int nbDays) {
        this.nbDays = nbDays;
    }
    
    public DesiredIntensity getIntensity() {
        return intensity;
    }
    
    public void setIntensity(DesiredIntensity intensity) {
        this.intensity = intensity;
    }
    
    public int getStarRating() {
        return starRating;
    }
    
    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }
    
    public String getTypeActivity() {
        return typeActivity;
    }
    
    public void setTypeActivity(String typeActivity) {
        this.typeActivity = typeActivity;
    }
    
    // ==================== Methods ====================
    
    /**
     * Valide la requête
     * @return true si tous les critères sont valides
     */
    public boolean validate() {
        // TODO: Implémenter la validation
        return false;
    }
    
    @Override
    public String toString() {
        return "OfferRequest{keywords='" + keywords + "', budget=" + budget +
               ", nbDays=" + nbDays + ", intensity=" + intensity +
               ", starRating=" + starRating + ", typeActivity='" + typeActivity + "'}";
    }
}
