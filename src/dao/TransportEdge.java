package dao;

import business.domain.TransportMode;

/**
 * Représente un lien de transport entre deux structures.
 * Format des noeuds : "H:id" pour hôtel, "S:id" pour site.
 * 
 * Exemples :
 * - "H:1" → "S:3" : De l'hôtel 1 au site 3
 * - "S:3" → "S:5" : Du site 3 au site 5
 * - "S:5" → "H:1" : Du site 5 à l'hôtel 1
 * 
 * @author Équipe Persistance
 */
public class TransportEdge {
    
    public final String fromNodeId;      // ex: "H:12" ou "S:33"
    public final String toNodeId;        // ex: "S:33" ou "H:12"
    public final TransportMode mode;     // FOOT/BUS/BOAT
    public final double cost;            // coût du segment (€)
    public final int durationMinutes;    // durée (minutes)
    
    /**
     * Constructeur.
     * 
     * @param fromNodeId Noeud de départ (format "H:id" ou "S:id")
     * @param toNodeId Noeud d'arrivée (format "H:id" ou "S:id")
     * @param mode Mode de transport
     * @param cost Coût du trajet en euros
     * @param durationMinutes Durée du trajet en minutes
     */
    public TransportEdge(String fromNodeId, String toNodeId, TransportMode mode,
                         double cost, int durationMinutes) {
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
        this.mode = mode;
        this.cost = cost;
        this.durationMinutes = durationMinutes;
    }
    
    @Override
    public String toString() {
        return String.format("TransportEdge{from=%s, to=%s, mode=%s, cost=%.2f€, duration=%dmin}",
                           fromNodeId, toNodeId, mode, cost, durationMinutes);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        TransportEdge other = (TransportEdge) obj;
        return fromNodeId.equals(other.fromNodeId) && 
               toNodeId.equals(other.toNodeId) && 
               mode == other.mode;
    }
    
    @Override
    public int hashCode() {
        int result = fromNodeId.hashCode();
        result = 31 * result + toNodeId.hashCode();
        result = 31 * result + mode.hashCode();
        return result;
    }
}
