package dao;

import business.domain.TransportMode;

import java.util.List;
import java.util.Set;

/**
 * Interface DAO pour l'accès aux données de transport.
 * Implémentation : JdbcTransportDao (package persistence.jdbc)
 * 
 * @author Équipe Persistance
 */
public interface TransportDao {
    
    /**
     * Retourne tous les liens de transport disponibles.
     * Requête SQL avec jointures sur Transport_Route, Hotel, Site, Transport_Mode.
     * 
     * Format du noeud : "H:id" pour hôtel, "S:id" pour site.
     * Exemple : "H:1" → "S:3" (de l'hôtel 1 au site 3)
     * 
     * @return Liste complète des TransportEdge
     */
    List<TransportEdge> findAllTransportEdges();
    
    /**
     * Retourne les liens de transport filtrés par modes autorisés.
     * Requête SQL avec filtre sur Transport_Mode.name.
     * 
     * @param allowedModes Ensemble des modes de transport autorisés (FOOT, BUS, BOAT)
     * @return Liste des TransportEdge correspondants
     */
    List<TransportEdge> findTransportEdgesByModes(Set<TransportMode> allowedModes);
}
