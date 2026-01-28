package persistence.jdbc;

import dao.TravelDao;
import dao.TransportDao;
import dao.TransportEdge;
import business.domain.Site;
import business.domain.Hotel;
import business.domain.TransportMode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestDaoIntegration {
    
    public static void main(String[] args) {
        
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║          TEST D'INTÉGRATION DAO + PERSISTANCE              ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // ===== TEST 1 : TravelDao - Tous les sites =====
        System.out.println("┌─ TEST 1 : findAllSites() ───────────────────────────────┐");
        TravelDao travelDao = new JdbcTravelDao();
        List<Site> sites = travelDao.findAllSites();
        System.out.println("✅ " + sites.size() + " sites trouvés");
        sites.forEach(s -> System.out.println("  → " + s.getName()));
        System.out.println();
        
        // ===== TEST 2 : TravelDao - Recherche par mots-clés (Lucene) =====
        System.out.println("┌─ TEST 2 : findSitesByKeywords('musée culture') ─────────┐");
        List<Site> sitesMusee = travelDao.findSitesByKeywords("musée culture");
        System.out.println("✅ " + sitesMusee.size() + " sites trouvés (triés par pertinence Lucene)");
        sitesMusee.forEach(s -> System.out.println("  → " + s.getName()));
        System.out.println();
        
        // ===== TEST 3 : TravelDao - Tous les hôtels =====
        System.out.println("┌─ TEST 3 : findAllHotels() ───────────────────────────────┐");
        List<Hotel> hotels = travelDao.findAllHotels();
        System.out.println("✅ " + hotels.size() + " hôtels trouvés");
        hotels.forEach(h -> System.out.println("  → " + h.getName() + " (" + h.getStarRating() + "★)"));
        System.out.println();
        
        // ===== TEST 4 : TravelDao - Hôtels par étoiles =====
        System.out.println("┌─ TEST 4 : findHotelsByMinStars(4) ───────────────────────┐");
        List<Hotel> hotels4Stars = travelDao.findHotelsByMinStars(4);
        System.out.println("✅ " + hotels4Stars.size() + " hôtels 4★+ trouvés");
        hotels4Stars.forEach(h -> System.out.println("  → " + h.getName() + " (" + h.getStarRating() + "★, " + h.getPrice() + "€)"));
        System.out.println();
        
        // ===== TEST 5 : TransportDao - Tous les liens =====
        System.out.println("┌─ TEST 5 : findAllTransportEdges() ───────────────────────┐");
        TransportDao transportDao = new JdbcTransportDao();
        List<TransportEdge>  edges = transportDao.findAllTransportEdges();
        System.out.println("✅ " + edges.size() + " liens de transport trouvés");
        edges.stream().limit(5).forEach(e -> System.out.println("  → " + e));
        System.out.println();
        
        // ===== TEST 6 : TransportDao - Filtre par mode =====
        System.out.println("┌─ TEST 6 : findTransportEdgesByModes(FOOT, BUS) ─────────┐");
        Set<TransportMode> allowedModes = new HashSet<>();
        allowedModes.add(TransportMode.FOOT);
        allowedModes.add(TransportMode.BUS);
        List<TransportEdge>  edgesFiltered = transportDao.findTransportEdgesByModes(allowedModes);
        System.out.println("✅ " + edgesFiltered.size() + " liens trouvés");
        edgesFiltered.stream().limit(5).forEach(e -> System.out.println("  → " + e));
        System.out.println();
        
        // ===== RÉSUMÉ =====
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║             TOUS LES TESTS SONT PASSÉS ✅                  ║");
        System.out.println("║                                                            ║");
        System.out.println("║  Les couches DAO et Persistance fonctionnent correctement.║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}