package persistence.jdbc;

import dao.TransportDao;
import dao.TransportEdge;
import business.domain.TransportMode;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implémentation JDBC de TransportDao.
 * Gère les liens de transport entre hôtels et sites.
 * 
 * CONFORME AU SCHÉMA RÉEL :
 * - Transport_Route avec origin_type/origin_id et destination_type/destination_id
 * - Structure polymorphe (HOTEL ou SITE)
 * 
 * @author Équipe Persistance
 */
public class JdbcTransportDao implements TransportDao {
    
    @Override
    public List<TransportEdge> findAllTransportEdges() {
        String query =
                "SELECT " +
                "    tr.id_route, " +
                "    tr.origin_type, " +
                "    tr.origin_id, " +
                "    tr.destination_type, " +
                "    tr.destination_id, " +
                "    tm.name AS transport_mode, " +
                "    tr.distance_km, " +
                "    tr.estimated_duration_minutes " +
                "FROM Transport_Route tr " +
                "JOIN Transport_Mode tm ON tr.recommended_transport_id = tm.id_transport " +
                "ORDER BY tr.id_route";
        
        JdbcExecuteQuery exec = new JdbcExecuteQuery();
        exec.prepareQuery(query);
        
        List<TransportEdge> edges = new ArrayList<TransportEdge>();
        try {
            exec.sqlExecutePreparedQuery();
            ResultSet rs = exec.getResultSet();
            while (rs.next()) {
                edges.add(mapTransportEdge(rs));
            }
        } catch (Exception e) {
            System.err.println("❌ SQL Exception in findAllTransportEdges: " + e.getMessage());
            e.printStackTrace();
        } finally {
            exec.close();
        }
        return edges;
    }
    
    @Override
    public List<TransportEdge> findTransportEdgesByModes(Set<TransportMode> allowedModes) {
        if (allowedModes == null || allowedModes.isEmpty()) {
            return findAllTransportEdges();
        }
        
        // Générer la clause IN dynamique
        String modeNames = allowedModes.stream()
            .map(Enum::name)
            .map(n -> "'" + n + "'")
            .collect(Collectors.joining(","));
        
        String query =
                "SELECT " +
                "    tr.id_route, " +
                "    tr.origin_type, " +
                "    tr.origin_id, " +
                "    tr.destination_type, " +
                "    tr.destination_id, " +
                "    tm.name AS transport_mode, " +
                "    tr.distance_km, " +
                "    tr.estimated_duration_minutes " +
                "FROM Transport_Route tr " +
                "JOIN Transport_Mode tm ON tr.recommended_transport_id = tm.id_transport " +
                "WHERE tm.name IN (" + modeNames + ") " +
                "ORDER BY tr.id_route";
        
        JdbcExecuteQuery exec = new JdbcExecuteQuery();
        exec.prepareQuery(query);
        
        List<TransportEdge> edges = new ArrayList<TransportEdge>();
        try {
            exec.sqlExecutePreparedQuery();
            ResultSet rs = exec.getResultSet();
            while (rs.next()) {
                edges.add(mapTransportEdge(rs));
            }
        } catch (Exception e) {
            System.err.println("❌ SQL Exception in findTransportEdgesByModes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            exec.close();
        }
        return edges;
    }
    
    // ==================== MÉTHODES PRIVÉES ====================
    
    /**
     * Mappe un ResultSet vers un TransportEdge.
     * Convertit la structure polymorphe (origin_type/origin_id) en format "H:id" ou "S:id".
     */
    private TransportEdge mapTransportEdge(ResultSet rs) throws Exception {
        // Récupérer les données du ResultSet
        String originType = rs.getString("origin_type");
        int originId = rs.getInt("origin_id");
        String destinationType = rs.getString("destination_type");
        int destinationId = rs.getInt("destination_id");
        String modeName = rs.getString("transport_mode");
        double distance = rs.getDouble("distance_km");
        int duration = rs.getInt("estimated_duration_minutes");
        
        // Construire les noeuds au format "H:id" ou "S:id"
        String fromNode = buildNodeId(originType, originId);
        String toNode = buildNodeId(destinationType, destinationId);
        
        // Récupérer le mode de transport
        TransportMode mode = TransportMode.valueOf(modeName);
        
        // Calculer le coût selon le mode
        double cost = mode.getPricePerKm() * distance;
        
        return new TransportEdge(fromNode, toNode, mode, cost, duration);
    }
    
    /**
     * Construit un identifiant de noeud au format "H:id" ou "S:id".
     * 
     * @param type "HOTEL" ou "SITE"
     * @param id ID de l'entité
     * @return String au format "H:id" ou "S:id"
     */
    private String buildNodeId(String type, int id) {
        if ("HOTEL".equalsIgnoreCase(type)) {
            return "H:" + id;
        } else if ("SITE".equalsIgnoreCase(type)) {
            return "S:" + id;
        } else {
            throw new IllegalArgumentException("Type inconnu : " + type);
        }
    }
}