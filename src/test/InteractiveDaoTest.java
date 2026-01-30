package test;

import dao.TravelDao;
import persistence.jdbc.JdbcTransportDao;
import persistence.jdbc.JdbcTravelDao;
import dao.TransportDao;
import dao.TransportEdge;
import business.domain.Site;
import business.domain.Hotel;
import business.domain.TransportMode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe de test interactive pour les couches DAO et Persistance.
 * Permet de tester les recherches via le terminal avec critères multiples.
 * 
 * FONCTIONNALITÉS :
 * - Recherche de sites (mots-clés, budget, type, combinée)
 * - Recherche d'hôtels (mots-clés, étoiles, budget, combinée)
 * - Recherche de transports (tous, par mode)
 * - Intersection de résultats selon plusieurs critères
 * 
 * @author Équipe Persistance
 */
public class InteractiveDaoTest {
    
    private static final Scanner scanner = new Scanner(System.in);
    private static final TravelDao travelDao = new JdbcTravelDao();
    private static final TransportDao transportDao = new JdbcTransportDao();
    
    public static void main(String[] args) {
        printHeader();
        
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Votre choix : ");
            
            switch (choice) {
                case 1: testSitesMenu(); break;
                case 2: testHotelsMenu(); break;
                case 3: testTransportsMenu(); break;
                case 4: testCombinedSearch(); break;
                case 0: running = false; break;
                default: System.out.println("❌ Choix invalide !"); break;
            }
        }
        
        System.out.println("\n✅ Au revoir !");
        scanner.close();
    }
    
    // ==================== MENUS ====================
    
    private static void printHeader() {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║       TEST INTERACTIF - COUCHES DAO & PERSISTANCE             ║");
        System.out.println("║              Tahiti Travel - AGP 2026                         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private static void printMainMenu() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│                    MENU PRINCIPAL                           │");
        System.out.println("├─────────────────────────────────────────────────────────────┤");
        System.out.println("│  1. Tests SITES (mots-clés, budget, type, combinés)        │");
        System.out.println("│  2. Tests HOTELS (mots-clés, étoiles, budget, combinés)    │");
        System.out.println("│  3. Tests TRANSPORTS (tous, filtrés par mode)              │");
        System.out.println("│  4. Recherche COMBINÉE (intersection multi-critères)       │");
        System.out.println("│  0. Quitter                                                 │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
    }
    
    private static void testSitesMenu() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│                    TESTS SITES                              │");
        System.out.println("├─────────────────────────────────────────────────────────────┤");
        System.out.println("│  1. Tous les sites                                          │");
        System.out.println("│  2. Recherche par MOTS-CLÉS (Lucene)                        │");
        System.out.println("│  3. Recherche par BUDGET (min-max)                          │");
        System.out.println("│  4. Recherche par TYPE (HISTORICAL/ACTIVITY)                │");
        System.out.println("│  5. Recherche COMBINÉE (intersection)                       │");
        System.out.println("│  0. Retour                                                  │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        
        int choice = readInt("Votre choix : ");
        
        switch (choice) {
            case 1: testAllSites(); break;
            case 2: testSitesByKeywords(); break;
            case 3: testSitesByBudget(); break;
            case 4: testSitesByType(); break;
            case 5: testSitesCombined(); break;
            case 0: break;
            default: System.out.println("❌ Choix invalide !"); break;
        }
    }
    
    private static void testHotelsMenu() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│                    TESTS HOTELS                             │");
        System.out.println("├─────────────────────────────────────────────────────────────┤");
        System.out.println("│  1. Tous les hôtels                                         │");
        System.out.println("│  2. Recherche par MOTS-CLÉS                                 │");
        System.out.println("│  3. Recherche par ÉTOILES (min)                             │");
        System.out.println("│  4. Recherche par BUDGET (min-max)                          │");
        System.out.println("│  5. Recherche COMBINÉE (intersection)                       │");
        System.out.println("│  0. Retour                                                  │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        
        int choice = readInt("Votre choix : ");
        
        switch (choice) {
            case 1: testAllHotels(); break;
            case 2: testHotelsByKeywords(); break;
            case 3: testHotelsByStars(); break;
            case 4: testHotelsByBudget(); break;
            case 5: testHotelsCombined(); break;
            case 0: break;
            default: System.out.println("❌ Choix invalide !"); break;
        }
    }
    
    private static void testTransportsMenu() {
        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│                   TESTS TRANSPORTS                          │");
        System.out.println("├─────────────────────────────────────────────────────────────┤");
        System.out.println("│  1. Tous les liens de transport                            │");
        System.out.println("│  2. Filtre par MODES (FOOT, BUS, BOAT)                      │");
        System.out.println("│  0. Retour                                                  │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");
        
        int choice = readInt("Votre choix : ");
        
        switch (choice) {
            case 1: testAllTransports(); break;
            case 2: testTransportsByModes(); break;
            case 0: break;
            default: System.out.println("❌ Choix invalide !"); break;
        }
    }
    
    // ==================== TESTS SITES ====================
    
    private static void testAllSites() {
        System.out.println("\n┌─ TOUS LES SITES ────────────────────────────────────────────┐");
        List<Site> sites = travelDao.findAllSites();
        displaySites(sites);
    }
    
    private static void testSitesByKeywords() {
        System.out.println("\n┌─ RECHERCHE PAR MOTS-CLÉS (Lucene) ──────────────────────────┐");
        System.out.print("Entrez les mots-clés (ex: plage culture musée) : ");
        String keywords = scanner.nextLine();
        
        System.out.println("\n⏳ Recherche Lucene en cours...");
        long start = System.currentTimeMillis();
        List<Site> sites = travelDao.findSitesByKeywords(keywords);
        long elapsed = System.currentTimeMillis() - start;
        
        System.out.println("✅ Recherche terminée en " + elapsed + "ms");
        System.out.println("📊 Résultats triés par PERTINENCE (score Lucene décroissant) :");
        displaySites(sites);
    }
    
    private static void testSitesByBudget() {
        System.out.println("\n┌─ RECHERCHE PAR BUDGET ──────────────────────────────────────┐");
        double min = readDouble("Prix minimum (€) : ");
        double max = readDouble("Prix maximum (€) : ");
        
        List<Site> sites = travelDao.findSitesByPriceRange(min, max);
        displaySites(sites);
    }
    
    private static void testSitesByType() {
        System.out.println("\n┌─ RECHERCHE PAR TYPE ────────────────────────────────────────┐");
        System.out.println("Types disponibles : HISTORICAL, ACTIVITY");
        System.out.print("Type : ");
        String type = scanner.nextLine().toUpperCase();
        
        List<Site> sites = travelDao.findSitesByType(type);
        displaySites(sites);
    }
    
    private static void testSitesCombined() {
        System.out.println("\n┌─ RECHERCHE COMBINÉE (INTERSECTION) ─────────────────────────┐");
        System.out.println("Cette recherche combine plusieurs critères et retourne");
        System.out.println("seulement les sites qui correspondent à TOUS les critères.\n");
        
        // Critère 1 : Mots-clés (optionnel)
        System.out.print("Mots-clés (appuyez Entrée pour ignorer) : ");
        String keywords = scanner.nextLine();
        List<Site> byKeywords = null;
        if (!keywords.trim().isEmpty()) {
            byKeywords = travelDao.findSitesByKeywords(keywords);
            System.out.println("  → " + byKeywords.size() + " sites trouvés par mots-clés");
        }
        
        // Critère 2 : Budget (optionnel)
        System.out.print("Prix minimum (€) (0 pour ignorer) : ");
        double min = readDouble("");
        System.out.print("Prix maximum (€) (0 pour ignorer) : ");
        double max = readDouble("");
        List<Site> byBudget = null;
        if (min > 0 || max > 0) {
            if (max == 0) max = Double.MAX_VALUE;
            byBudget = travelDao.findSitesByPriceRange(min, max);
            System.out.println("  → " + byBudget.size() + " sites trouvés par budget");
        }
        
        // Critère 3 : Type (optionnel)
        System.out.print("Type (HISTORICAL/ACTIVITY, Entrée pour ignorer) : ");
        String type = scanner.nextLine().toUpperCase();
        List<Site> byType = null;
        if (!type.trim().isEmpty()) {
            byType = travelDao.findSitesByType(type);
            System.out.println("  → " + byType.size() + " sites trouvés par type");
        }
        
        // INTERSECTION
        System.out.println("\n⏳ Calcul de l'intersection...");
        List<Site> result = intersectSites(byKeywords, byBudget, byType);
        
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                  RÉSULTATS COMBINÉS                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        displaySites(result);
    }
    
    // ==================== TESTS HOTELS ====================
    
    private static void testAllHotels() {
        System.out.println("\n┌─ TOUS LES HÔTELS ───────────────────────────────────────────┐");
        List<Hotel> hotels = travelDao.findAllHotels();
        displayHotels(hotels);
    }
    
    private static void testHotelsByKeywords() {
        System.out.println("\n┌─ RECHERCHE PAR MOTS-CLÉS ───────────────────────────────────┐");
        System.out.print("Entrez les mots-clés (ex: luxe plage) : ");
        String keywords = scanner.nextLine();
        
        List<Hotel> hotels = travelDao.findHotelsByKeywords(keywords);
        displayHotels(hotels);
    }
    
    private static void testHotelsByStars() {
        System.out.println("\n┌─ RECHERCHE PAR ÉTOILES ─────────────────────────────────────┐");
        int minStars = readInt("Nombre minimum d'étoiles (1-5) : ");
        
        List<Hotel> hotels = travelDao.findHotelsByMinStars(minStars);
        displayHotels(hotels);
    }
    
    private static void testHotelsByBudget() {
        System.out.println("\n┌─ RECHERCHE PAR BUDGET ──────────────────────────────────────┐");
        double min = readDouble("Prix minimum par nuit (€) : ");
        double max = readDouble("Prix maximum par nuit (€) : ");
        
        List<Hotel> hotels = travelDao.findHotelsByPriceRange(min, max);
        displayHotels(hotels);
    }
    
    private static void testHotelsCombined() {
        System.out.println("\n┌─ RECHERCHE COMBINÉE (INTERSECTION) ─────────────────────────┐");
        System.out.println("Cette recherche combine plusieurs critères et retourne");
        System.out.println("seulement les hôtels qui correspondent à TOUS les critères.\n");
        
        // Critère 1 : Mots-clés
        System.out.print("Mots-clés (Entrée pour ignorer) : ");
        String keywords = scanner.nextLine();
        List<Hotel> byKeywords = null;
        if (!keywords.trim().isEmpty()) {
            byKeywords = travelDao.findHotelsByKeywords(keywords);
            System.out.println("  → " + byKeywords.size() + " hôtels trouvés par mots-clés");
        }
        
        // Critère 2 : Étoiles
        System.out.print("Étoiles minimum (0 pour ignorer) : ");
        int minStars = readInt("");
        List<Hotel> byStars = null;
        if (minStars > 0) {
            byStars = travelDao.findHotelsByMinStars(minStars);
            System.out.println("  → " + byStars.size() + " hôtels trouvés par étoiles");
        }
        
        // Critère 3 : Budget
        System.out.print("Prix minimum (€) (0 pour ignorer) : ");
        double min = readDouble("");
        System.out.print("Prix maximum (€) (0 pour ignorer) : ");
        double max = readDouble("");
        List<Hotel> byBudget = null;
        if (min > 0 || max > 0) {
            if (max == 0) max = Double.MAX_VALUE;
            byBudget = travelDao.findHotelsByPriceRange(min, max);
            System.out.println("  → " + byBudget.size() + " hôtels trouvés par budget");
        }
        
        // INTERSECTION
        System.out.println("\n⏳ Calcul de l'intersection...");
        List<Hotel> result = intersectHotels(byKeywords, byStars, byBudget);
        
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                  RÉSULTATS COMBINÉS                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        displayHotels(result);
    }
    
    // ==================== TESTS TRANSPORTS ====================
    
    private static void testAllTransports() {
        System.out.println("\n┌─ TOUS LES LIENS DE TRANSPORT ───────────────────────────────┐");
        List<TransportEdge> edges = transportDao.findAllTransportEdges();
        displayTransports(edges);
    }
    
    private static void testTransportsByModes() {
        System.out.println("\n┌─ FILTRE PAR MODES ──────────────────────────────────────────┐");
        System.out.println("Modes disponibles : FOOT, BUS, BOAT");
        System.out.print("Entrez les modes séparés par des virgules (ex: FOOT,BUS) : ");
        String modesStr = scanner.nextLine();
        
        Set<TransportMode> modes = new HashSet<>();
        for (String m : modesStr.split(",")) {
            try {
                modes.add(TransportMode.valueOf(m.trim().toUpperCase()));
            } catch (Exception e) {
                System.out.println("⚠️  Mode ignoré : " + m);
            }
        }
        
        List<TransportEdge> edges = transportDao.findTransportEdgesByModes(modes);
        displayTransports(edges);
    }
    
    // ==================== RECHERCHE COMBINÉE GLOBALE ====================
    
    private static void testCombinedSearch() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║           RECHERCHE COMBINÉE SITES + HÔTELS               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("Cette recherche vous permet de chercher des sites ET des hôtels");
        System.out.println("avec plusieurs critères, puis affiche les résultats combinés.\n");
        
        // SITES
        System.out.println("┌─ CRITÈRES SITES ────────────────────────────────────────────┐");
        System.out.print("Mots-clés sites (Entrée pour ignorer) : ");
        String siteKeywords = scanner.nextLine();
        System.out.print("Budget min sites (€) (0 pour ignorer) : ");
        double siteMinBudget = readDouble("");
        System.out.print("Budget max sites (€) (0 pour ignorer) : ");
        double siteMaxBudget = readDouble("");
        
        // HOTELS
        System.out.println("\n┌─ CRITÈRES HÔTELS ───────────────────────────────────────────┐");
        System.out.print("Mots-clés hôtels (Entrée pour ignorer) : ");
        String hotelKeywords = scanner.nextLine();
        System.out.print("Étoiles min (0 pour ignorer) : ");
        int minStars = readInt("");
        System.out.print("Budget min hôtels (€) (0 pour ignorer) : ");
        double hotelMinBudget = readDouble("");
        System.out.print("Budget max hôtels (€) (0 pour ignorer) : ");
        double hotelMaxBudget = readDouble("");
        
        // RECHERCHE
        System.out.println("\n⏳ Recherche en cours...");
        
        List<Site> sites = new ArrayList<>();
        if (!siteKeywords.trim().isEmpty()) {
            sites = travelDao.findSitesByKeywords(siteKeywords);
        } else {
            sites = travelDao.findAllSites();
        }
        
        if (siteMinBudget > 0 || siteMaxBudget > 0) {
            double max = siteMaxBudget == 0 ? Double.MAX_VALUE : siteMaxBudget;
            List<Site> byBudget = travelDao.findSitesByPriceRange(siteMinBudget, max);
            sites = intersectSites(sites, byBudget, null);
        }
        
        List<Hotel> hotels = new ArrayList<>();
        if (!hotelKeywords.trim().isEmpty()) {
            hotels = travelDao.findHotelsByKeywords(hotelKeywords);
        } else {
            hotels = travelDao.findAllHotels();
        }
        
        if (minStars > 0) {
            List<Hotel> byStars = travelDao.findHotelsByMinStars(minStars);
            hotels = intersectHotels(hotels, byStars, null);
        }
        
        if (hotelMinBudget > 0 || hotelMaxBudget > 0) {
            double max = hotelMaxBudget == 0 ? Double.MAX_VALUE : hotelMaxBudget;
            List<Hotel> byBudget = travelDao.findHotelsByPriceRange(hotelMinBudget, max);
            hotels = intersectHotels(hotels, byBudget, null);
        }
        
        // AFFICHAGE
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                    RÉSULTATS GLOBAUX                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        
        System.out.println("\n🏛️  SITES (" + sites.size() + " résultats) :");
        displaySites(sites);
        
        System.out.println("\n🏨 HÔTELS (" + hotels.size() + " résultats) :");
        displayHotels(hotels);
    }
    
    // ==================== AFFICHAGE ====================
    
    private static void displaySites(List<Site> sites) {
        if (sites.isEmpty()) {
            System.out.println("❌ Aucun site trouvé.");
            return;
        }
        
        System.out.println("\n✅ " + sites.size() + " site(s) trouvé(s) :");
        System.out.println("┌────┬────────────────────────────────────┬──────────┬────────────┐");
        System.out.println("│ ID │ Nom                                │ Prix (€) │ Type       │");
        System.out.println("├────┼────────────────────────────────────┼──────────┼────────────┤");
        
        for (Site s : sites) {
            String name = truncate(s.getName(), 34);
            String type = s.getClass().getSimpleName().replace("Site", "");
            System.out.printf("│ %-2d │ %-34s │ %8.2f │ %-10s │%n",
                s.getId(), name, s.getPrice(), type);
        }
        System.out.println("└────┴────────────────────────────────────┴──────────┴────────────┘");
    }
    
    private static void displayHotels(List<Hotel> hotels) {
        if (hotels.isEmpty()) {
            System.out.println("❌ Aucun hôtel trouvé.");
            return;
        }
        
        System.out.println("\n✅ " + hotels.size() + " hôtel(s) trouvé(s) :");
        System.out.println("┌────┬────────────────────────────────────┬──────────┬────────┬──────────────┐");
        System.out.println("│ ID │ Nom                                │ Prix/nuit│ Étoiles│ Plage        │");
        System.out.println("├────┼────────────────────────────────────┼──────────┼────────┼──────────────┤");
        
        for (Hotel h : hotels) {
            String name = truncate(h.getName(), 34);
            String beach = truncate(h.getBeachName() != null ? h.getBeachName() : "N/A", 12);
            String stars = "";
            for (int i = 0; i < h.getStarRating(); i++) {
                stars += "★";
            }
            System.out.printf("│ %-2d │ %-34s │ %8.2f │ %-6s │ %-12s │%n",
                h.getId(), name, h.getPrice(), stars, beach);
        }
        System.out.println("└────┴────────────────────────────────────┴──────────┴────────┴──────────────┘");
    }
    
    private static void displayTransports(List<TransportEdge> edges) {
        if (edges.isEmpty()) {
            System.out.println("❌ Aucun lien de transport trouvé.");
            return;
        }
        
        System.out.println("\n✅ " + edges.size() + " lien(s) de transport trouvé(s) :");
        System.out.println("┌──────────┬──────────┬──────────┬──────────┬────────────┐");
        System.out.println("│ Origine  │ Destin.  │ Mode     │ Coût (€) │ Durée (min)│");
        System.out.println("├──────────┼──────────┼──────────┼──────────┼────────────┤");
        
        for (TransportEdge e : edges.stream().limit(20).collect(Collectors.toList())) {
            System.out.printf("│ %-8s │ %-8s │ %-8s │ %8.2f │ %10d │%n",
                e.fromNodeId, e.toNodeId, e.mode, e.cost, e.durationMinutes);
        }
        
        if (edges.size() > 20) {
            System.out.println("│ ... (" + (edges.size() - 20) + " autres résultats) ...                              │");
        }
        System.out.println("└──────────┴──────────┴──────────┴──────────┴────────────┘");
    }
    
    // ==================== INTERSECTION ====================
    
    @SafeVarargs
    private static List<Site> intersectSites(List<Site>... lists) {
        List<Site> result = null;
        
        for (List<Site> list : lists) {
            if (list == null) continue;
            
            if (result == null) {
                result = new ArrayList<>(list);
            } else {
                Set<Integer> ids = list.stream()
                    .map(Site::getId)
                    .collect(Collectors.toSet());
                result = result.stream()
                    .filter(s -> ids.contains(s.getId()))
                    .collect(Collectors.toList());
            }
        }
        
        return result != null ? result : new ArrayList<>();
    }
    
    @SafeVarargs
    private static List<Hotel> intersectHotels(List<Hotel>... lists) {
        List<Hotel> result = null;
        
        for (List<Hotel> list : lists) {
            if (list == null) continue;
            
            if (result == null) {
                result = new ArrayList<>(list);
            } else {
                Set<Integer> ids = list.stream()
                    .map(Hotel::getId)
                    .collect(Collectors.toSet());
                result = result.stream()
                    .filter(h -> ids.contains(h.getId()))
                    .collect(Collectors.toList());
            }
        }
        
        return result != null ? result : new ArrayList<>();
    }
    
    // ==================== UTILITAIRES ====================
    
    private static String truncate(String str, int maxLen) {
        if (str == null) return "";
        return str.length() <= maxLen ? str : str.substring(0, maxLen - 3) + "...";
    }
    
    private static int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("❌ Nombre invalide. " + prompt);
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Consommer le \n
        return value;
    }
    
    private static double readDouble(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            scanner.next();
            System.out.print("❌ Nombre invalide. " + prompt);
        }
        double value = scanner.nextDouble();
        scanner.nextLine(); // Consommer le \n
        return value;
    }
}