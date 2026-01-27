package business.domain;

/**
 * Classe abstraite de base pour toutes les structures localisables
 * (sites touristiques, hôtels, etc.)
 */
public abstract class Structure {
    
    private int id;
    private String name;
    private double price;
    private Position position;
    
    // ==================== Constructeurs ====================
    
    public Structure() {
    }
    
    public Structure(int id, String name, double price, Position position) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.position = position;
    }
    
    // ==================== Getters & Setters ====================
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(Position position) {
        this.position = position;
    }
    
    // ==================== Methods ====================
    
    @Override
    public String toString() {
        return "Structure{id=" + id + ", name='" + name + "', price=" + price + "}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Structure other = (Structure) obj;
        return this.id == other.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
