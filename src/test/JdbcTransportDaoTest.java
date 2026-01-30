package test;

import dao.TransportDao;
import dao.TransportEdge;
import dao.TransportMode;
import persistence.jdbc.JdbcTransportDao;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour JdbcTransportDao
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JdbcTransportDaoTest {
    
    private static TransportDao transportDao;
    
    @BeforeAll
    public static void setUpClass() {
        System.out.println("=== Initialisation des tests JdbcTransportDao ===");
        transportDao = new JdbcTransportDao();
    }
    
    @AfterAll
    public static void tearDownClass() {
        System.out.println("=== Fin des tests JdbcTransportDao ===");
    }
    
    @Test
    @Order(1)
    @DisplayName("Test findAllTransportEdges - Doit retourner toutes les routes")
    public void testFindAllTransportEdges() {
        System.out.println("\n--- Test findAllTransportEdges ---");
        
        List<TransportEdge> edges = transportDao.findAllTransportEdges();
        
        assertNotNull(edges, "La liste ne doit pas être null");
        assertFalse(edges.isEmpty(), "La liste ne doit pas être vide");
        
        System.out.println("Nombre de routes trouvées : " + edges.size());
        
        for (TransportEdge edge : edges) {
            assertNotNull(edge.getFromNodeId(), "fromNodeId ne doit pas être null");
            assertNotNull(edge.getToNodeId(), "toNodeId ne doit pas être null");
            assertNotNull(edge.getMode(), "mode ne doit pas être null");
            assertTrue(edge.getCost() >= 0, "cost doit être >= 0");
            assertTrue(edge.getDurationMinutes() >= 0, "duration doit être >= 0");
            
            System.out.printf("  - %s → %s : %s (%.2f€, %d min)%n",
                edge.getFromNodeId(), edge.getToNodeId(),
                edge.getMode(), edge.getCost(), edge.getDurationMinutes());
        }
    }
    
    @Test
    @Order(2)
    @DisplayName("Test findTransportEdgesByModes - Mode BUS uniquement")
    public void testFindTransportEdgesByModes_BusOnly() {
        System.out.println("\n--- Test findTransportEdgesByModes (BUS) ---");
        
        Set<TransportMode> modes = new HashSet<>();
        modes.add(TransportMode.BUS);
        
        List<TransportEdge> edges = transportDao.findTransportEdgesByModes(modes);
        
        assertNotNull(edges, "La liste ne doit pas être null");
        System.out.printf("Routes en BUS : %d%n", edges.size());
        
        for (TransportEdge edge : edges) {
            assertEquals(TransportMode.BUS, edge.getMode(), 
                "Toutes les routes doivent être en BUS");
            System.out.printf("  - %s → %s (%.2f€)%n",
                edge.getFromNodeId(), edge.getToNodeId(), edge.getCost());
        }
    }
    
    @Test
    @Order(3)
    @DisplayName("Test findTransportEdgesByModes - Modes BUS et BOAT")
    public void testFindTransportEdgesByModes_Multiple() {
        System.out.println("\n--- Test findTransportEdgesByModes (BUS, BOAT) ---");
        
        Set<TransportMode> modes = new HashSet<>();
        modes.add(TransportMode.BUS);
        modes.add(TransportMode.BOAT);
        
        List<TransportEdge> edges = transportDao.findTransportEdgesByModes(modes);
        
        assertNotNull(edges, "La liste ne doit pas être null");
        System.out.printf("Routes en BUS ou BOAT : %d%n", edges.size());
        
        for (TransportEdge edge : edges) {
            assertTrue(edge.getMode() == TransportMode.BUS || 
                      edge.getMode() == TransportMode.BOAT,
                "Le mode doit être BUS ou BOAT");
            System.out.printf("  - %s → %s : %s%n",
                edge.getFromNodeId(), edge.getToNodeId(), edge.getMode());
        }
    }
    
    @Test
    @Order(4)
    @DisplayName("Test findTransportEdgesByModes - Mode FOOT")
    public void testFindTransportEdgesByModes_Foot() {
        System.out.println("\n--- Test findTransportEdgesByModes (FOOT) ---");
        
        Set<TransportMode> modes = new HashSet<>();
        modes.add(TransportMode.FOOT);
        
        List<TransportEdge> edges = transportDao.findTransportEdgesByModes(modes);
        
        assertNotNull(edges, "La liste ne doit pas être null");
        System.out.printf("Routes à pied : %d%n", edges.size());
        
        for (TransportEdge edge : edges) {
            assertEquals(TransportMode.FOOT, edge.getMode());
            // Les routes à pied devraient avoir un coût de 0
            assertEquals(0.0, edge.getCost(), 0.01, 
                "Les routes à pied doivent être gratuites");
            System.out.printf("  - %s → %s (%d min)%n",
                edge.getFromNodeId(), edge.getToNodeId(), 
                edge.getDurationMinutes());
        }
    }
    
    @Test
    @Order(5)
    @DisplayName("Test findTransportEdgesByModes - Tous les modes")
    public void testFindTransportEdgesByModes_All() {
        System.out.println("\n--- Test findTransportEdgesByModes (TOUS) ---");
        
        Set<TransportMode> modes = new HashSet<>();
        modes.add(TransportMode.FOOT);
        modes.add(TransportMode.BUS);
        modes.add(TransportMode.BOAT);
        
        List<TransportEdge> edgesFiltered = transportDao.findTransportEdgesByModes(modes);
        List<TransportEdge> edgesAll = transportDao.findAllTransportEdges();
        
        assertNotNull(edgesFiltered, "La liste filtrée ne doit pas être null");
        assertNotNull(edgesAll, "La liste complète ne doit pas être null");
        
        assertEquals(edgesAll.size(), edgesFiltered.size(), 
            "Filtrer par tous les modes doit retourner toutes les routes");
        
        System.out.printf("Routes avec tous les modes : %d%n", edgesFiltered.size());
    }
    
    @Test
    @Order(6)
    @DisplayName("Test findTransportEdgesByModes - Set vide")
    public void testFindTransportEdgesByModes_Empty() {
        System.out.println("\n--- Test findTransportEdgesByModes (vide) ---");
        
        Set<TransportMode> modes = new HashSet<>();
        
        List<TransportEdge> edges = transportDao.findTransportEdgesByModes(modes);
        
        assertNotNull(edges, "La liste ne doit pas être null");
        assertTrue(edges.isEmpty(), "Doit retourner une liste vide si aucun mode spécifié");
        System.out.println("Résultat attendu : liste vide");
    }
    
    @Test
    @Order(7)
    @DisplayName("Test TransportEdge - Validation des données")
    public void testTransportEdge_DataValidation() {
        System.out.println("\n--- Test TransportEdge (validation) ---");
        
        List<TransportEdge> edges = transportDao.findAllTransportEdges();
        
        int validEdges = 0;
        int invalidEdges = 0;
        
        for (TransportEdge edge : edges) {
            boolean isValid = true;
            
            // Vérifications
            if (edge.getFromNodeId() == null || edge.getFromNodeId().isEmpty()) {
                System.err.println("  ERREUR : fromNodeId vide");
                isValid = false;
            }
            if (edge.getToNodeId() == null || edge.getToNodeId().isEmpty()) {
                System.err.println("  ERREUR : toNodeId vide");
                isValid = false;
            }
            if (edge.getCost() < 0) {
                System.err.println("  ERREUR : cost négatif");
                isValid = false;
            }
            if (edge.getDurationMinutes() < 0) {
                System.err.println("  ERREUR : duration négative");
                isValid = false;
            }
            
            if (isValid) {
                validEdges++;
            } else {
                invalidEdges++;
            }
        }
        
        System.out.printf("Routes valides : %d%n", validEdges);
        System.out.printf("Routes invalides : %d%n", invalidEdges);
        
        assertEquals(0, invalidEdges, "Toutes les routes doivent être valides");
    }
}
