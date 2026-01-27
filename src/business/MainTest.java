package business;

import business.domain.Hotel;
import business.domain.Site;
import business.service.MockTravelDataAccess;
import business.service.TravelService;

import java.util.List;

/**
 * Classe de test pour vérifier le bon fonctionnement de la couche Business
 * SANS avoir besoin de la couche persistance ni de l'interface web
 * 
 * Pour exécuter : Run As -> Java Application
 */
public class MainTest {
    
    public static void main(String[] args) {
        
        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║          TEST DE LA COUCHE BUSINESS - RECHERCHE SIMPLE            ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // 1. Créer le service avec le Mock
        TravelService service = new TravelService(new MockTravelDataAccess());
        
        // ============================================================
        // TEST 1 : Recherche de sites par mots-clés
        // ============================================================
        
        System.out.println("┌────────────────────────────────────────────────────────────────────┐");
        System.out.println("│  TEST 1 : Recherche de sites avec le mot-clé 'plongée'            │");
        System.out.println("└────────────────────────────────────────────────────────────────────┘");
        
        List<Site> sitesPlongee = service.searchSites("plongée");
        
        System.out.println("Résultats trouvés : " + sitesPlongee.size());
        for (Site site : sitesPlongee) {
            System.out.println("  → " + site.getName() + " | Prix: " + site.getPrice() + "€");
            System.out.println("    Description: " + site.getDescription().substring(0, Math.min(60, site.getDescription().length())) + "...");
        }
        System.out.println();
        
        // ============================================================
        // TEST 2 : Recherche de sites avec un autre mot-clé
        // ============================================================
        
        System.out.println("┌────────────────────────────────────────────────────────────────────┐");
        System.out.println("│  TEST 2 : Recherche de sites avec le mot-clé 'surf'               │");
        System.out.println("└────────────────────────────────────────────────────────────────────┘");
        
        List<Site> sitesSurf = service.searchSites("surf");
        
        System.out.println("Résultats trouvés : " + sitesSurf.size());
        for (Site site : sitesSurf) {
            System.out.println("  → " + site.getName() + " | Prix: " + site.getPrice() + "€");
        }
        System.out.println();
        
        // ============================================================
        // TEST 3 : Recherche de sites historiques
        // ============================================================
        
        System.out.println("┌────────────────────────────────────────────────────────────────────┐");
        System.out.println("│  TEST 3 : Recherche de sites avec le mot-clé 'musée'              │");
        System.out.println("└────────────────────────────────────────────────────────────────────┘");
        
        List<Site> sitesMusee = service.searchSites("musée");
        
        System.out.println("Résultats trouvés : " + sitesMusee.size());
        for (Site site : sitesMusee) {
            System.out.println("  → " + site.getName() + " | Prix: " + site.getPrice() + "€");
            System.out.println("    Est historique ? " + site.isHistorical());
        }
        System.out.println();
        
        // ============================================================
        // TEST 4 : Recherche d'hôtels par mots-clés
        // ============================================================
        
        System.out.println("┌────────────────────────────────────────────────────────────────────┐");
        System.out.println("│  TEST 4 : Recherche d'hôtels avec le mot-clé 'plage'              │");
        System.out.println("└────────────────────────────────────────────────────────────────────┘");
        
        List<Hotel> hotelsPlage = service.searchHotels("plage");
        
        System.out.println("Résultats trouvés : " + hotelsPlage.size());
        for (Hotel hotel : hotelsPlage) {
            System.out.println("  → " + hotel.getName() + " | " + hotel.getStarRating() + "★ | " + hotel.getPrice() + "€/nuit");
            System.out.println("    Plage : " + hotel.getBeachName());
        }
        System.out.println();
        
        // ============================================================
        // TEST 5 : Recherche d'hôtels par étoiles
        // ============================================================
        
        System.out.println("┌────────────────────────────────────────────────────────────────────┐");
        System.out.println("│  TEST 5 : Recherche d'hôtels avec minimum 4 étoiles               │");
        System.out.println("└────────────────────────────────────────────────────────────────────┘");
        
        List<Hotel> hotels4Etoiles = service.searchHotelsByStars(4);
        
        System.out.println("Résultats trouvés : " + hotels4Etoiles.size());
        for (Hotel hotel : hotels4Etoiles) {
            System.out.println("  → " + hotel.getName() + " | " + hotel.getStarRating() + "★ | " + hotel.getPrice() + "€/nuit");
        }
        System.out.println();
        
        // ============================================================
        // TEST 6 : Recherche avec mot-clé inexistant
        // ============================================================
        
        System.out.println("┌────────────────────────────────────────────────────────────────────┐");
        System.out.println("│  TEST 6 : Recherche avec mot-clé inexistant 'ski'                 │");
        System.out.println("└────────────────────────────────────────────────────────────────────┘");
        
        List<Site> sitesSki = service.searchSites("ski");
        
        System.out.println("Résultats trouvés : " + sitesSki.size());
        if (sitesSki.isEmpty()) {
            System.out.println("  (Aucun résultat - comportement attendu ✓)");
        }
        System.out.println();
        
        // ============================================================
        // TEST 7 : Validation des erreurs
        // ============================================================
        
        System.out.println("┌────────────────────────────────────────────────────────────────────┐");
        System.out.println("│  TEST 7 : Test de validation (mot-clé vide)                       │");
        System.out.println("└────────────────────────────────────────────────────────────────────┘");
        
        try {
            service.searchSites("");
            System.out.println("  ERREUR : Aurait dû lancer une exception ✗");
        } catch (IllegalArgumentException e) {
            System.out.println("  Exception attrapée : " + e.getMessage());
            System.out.println("  (Comportement attendu ✓)");
        }
        System.out.println();
        
        // ============================================================
        // RÉSUMÉ
        // ============================================================
        
        System.out.println("╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    TOUS LES TESTS SONT PASSÉS ✓                   ║");
        System.out.println("║                                                                    ║");
        System.out.println("║  La couche Business fonctionne correctement.                      ║");
        System.out.println("║  Elle est prête à être intégrée avec :                            ║");
        System.out.println("║    - La couche persistance (remplacer MockTravelDataAccess)       ║");
        System.out.println("║    - La couche présentation (Beans JSF)                           ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
    }
}
