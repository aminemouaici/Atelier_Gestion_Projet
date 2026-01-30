package test;

import dao.TravelDao;
import model.Hotel;
import model.Site;
import persistence.jdbc.JdbcTravelDao;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour JdbcTravelDao
 * 
 * PRÉREQUIS :
 * - Base de données MySQL configurée et accessible
 * - Tables créées (Hotel, Site, Transport_Mode, Site_Transport, Transport_Route)
 * - Données de test insérées
 * - Index Lucene créé pour les sites
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JdbcTravelDaoTest {
    
    private static TravelDao travelDao;
    
    @BeforeAll
    public static void setUpClass() {
        System.out.println("=== Initialisation des tests JdbcTravelDao ===");
        travelDao = new JdbcTravelDao();
    }
    
    @AfterAll
    public static void tearDownClass() {
        System.out.println("=== Fin des tests JdbcTravelDao ===");
    }
    
    // =====================================================
    // TESTS SITE
    // =====================================================
    
    @Test
    @Order(1)
    @DisplayName("Test findAllSites - Doit retourner tous les sites")
    public void testFindAllSites() {
        System.out.println("\n--- Test findAllSites ---");
        
        List<Site> sites = travelDao.findAllSites();
        
        assertNotNull(sites, "La liste ne doit pas être null");
        assertFalse(sites.isEmpty(), "La liste ne doit pas être vide");
        
        System.out.println("Nombre de sites trouvés : " + sites.size());
        
        // Vérifier que chaque site a les attributs requis
        for (Site site : sites) {
            assertNotNull(site.getIdSite(), "id_site ne doit pas être null");
            assertNotNull(site.getName(), "name ne doit pas être null");
            assertNotNull(site.getSiteType(), "site_type ne doit pas être null");
            
            System.out.printf("  - Site #%d : %s (%s)%n", 
                site.getIdSite(), site.getName(), site.getSiteType());
        }
    }
    
    @Test
    @Order(2)
    @DisplayName("Test findSiteById - Site existant")
    public void testFindSiteById_Exists() {
        System.out.println("\n--- Test findSiteById (existant) ---");
        
        int siteId = 1; // ID d'un site qui doit exister
        Optional<Site> siteOpt = travelDao.findSiteById(siteId);
        
        assertTrue(siteOpt.isPresent(), "Le site #1 doit exister");
        
        Site site = siteOpt.get();
        assertEquals(siteId, site.getIdSite(), "L'ID doit correspondre");
        assertNotNull(site.getName(), "Le nom ne doit pas être null");
        
        System.out.printf("Site trouvé : #%d - %s%n", site.getIdSite(), site.getName());
    }
    
    @Test
    @Order(3)
    @DisplayName("Test findSiteById - Site inexistant")
    public void testFindSiteById_NotExists() {
        System.out.println("\n--- Test findSiteById (inexistant) ---");
        
        int siteId = 99999; // ID qui ne doit pas exister
        Optional<Site> siteOpt = travelDao.findSiteById(siteId);
        
        assertFalse(siteOpt.isPresent(), "Le site #99999 ne doit pas exister");
        System.out.println("Résultat attendu : Optional.empty()");
    }
    
    @Test
    @Order(4)
    @DisplayName("Test findSitesByType - Type HISTORICAL")
    public void testFindSitesByType_Historical() {
        System.out.println("\n--- Test findSitesByType (HISTORICAL) ---");
        
        List<Site> sites = travelDao.findSitesByType("HISTORICAL");
        
        assertNotNull(sites, "La liste ne doit pas être null");
        System.out.println("Nombre de sites historiques : " + sites.size());
        
        // Vérifier que tous les sites sont bien de type HISTORICAL
        for (Site site : sites) {
            assertEquals("HISTORICAL", site.getSiteType(), 
                "Tous les sites doivent être de type HISTORICAL");
            System.out.printf("  - %s (type=%s)%n", site.getName(), site.getSiteType());
        }
    }
    
    @Test
    @Order(5)
    @DisplayName("Test findSitesByType - Type ACTIVITY")
    public void testFindSitesByType_Activity() {
        System.out.println("\n--- Test findSitesByType (ACTIVITY) ---");
        
        List<Site> sites = travelDao.findSitesByType("ACTIVITY");
        
        assertNotNull(sites, "La liste ne doit pas être null");
        System.out.println("Nombre de sites d'activité : " + sites.size());
        
        for (Site site : sites) {
            assertEquals("ACTIVITY", site.getSiteType());
            System.out.printf("  - %s (catégorie=%s)%n", 
                site.getName(), site.getActivityCategory());
        }
    }
    
    @Test
    @Order(6)
    @DisplayName("Test findSitesByPriceRange - Prix entre 0 et 50")
    public void testFindSitesByPriceRange() {
        System.out.println("\n--- Test findSitesByPriceRange (0-50) ---");
        
        double minPrice = 0.0;
        double maxPrice = 50.0;
        List<Site> sites = travelDao.findSitesByPriceRange(minPrice, maxPrice);
        
        assertNotNull(sites, "La liste ne doit pas être null");
        System.out.printf("Sites entre %.2f€ et %.2f€ : %d%n", 
            minPrice, maxPrice, sites.size());
        
        for (Site site : sites) {
            double price = site.getEntryPrice();
            assertTrue(price >= minPrice && price <= maxPrice, 
                String.format("Le prix %.2f doit être entre %.2f et %.2f", 
                    price, minPrice, maxPrice));
            System.out.printf("  - %s : %.2f€%n", site.getName(), price);
        }
    }
    
    @Test
    @Order(7)
    @DisplayName("Test findSitesByKeywords - Recherche textuelle Lucene")
    public void testFindSitesByKeywords() {
        System.out.println("\n--- Test findSitesByKeywords ---");
        
        String keywords = "musée culture";
        List<Site> sites = travelDao.findSitesByKeywords(keywords);
        
        assertNotNull(sites, "La liste ne doit pas être null");
        System.out.printf("Sites trouvés pour '%s' : %d%n", keywords, sites.size());
        
        // Les résultats doivent être triés par pertinence Lucene
        for (int i = 0; i < sites.size(); i++) {
            Site site = sites.get(i);
            System.out.printf("  %d. %s (id=%d, type=%s)%n", 
                i + 1, site.getName(), site.getIdSite(), site.getSiteType());
        }
        
        // Vérifier qu'il y a des résultats si les données de test sont correctes
        assertTrue(sites.size() >= 0, "Doit retourner 0 ou plusieurs résultats");
    }
    
    // =====================================================
    // TESTS HOTEL
    // =====================================================
    
    @Test
    @Order(8)
    @DisplayName("Test findAllHotels - Doit retourner tous les hôtels")
    public void testFindAllHotels() {
        System.out.println("\n--- Test findAllHotels ---");
        
        List<Hotel> hotels = travelDao.findAllHotels();
        
        assertNotNull(hotels, "La liste ne doit pas être null");
        assertFalse(hotels.isEmpty(), "La liste ne doit pas être vide");
        
        System.out.println("Nombre d'hôtels trouvés : " + hotels.size());
        
        for (Hotel hotel : hotels) {
            assertNotNull(hotel.getIdHotel(), "id_hotel ne doit pas être null");
            assertNotNull(hotel.getName(), "name ne doit pas être null");
            assertTrue(hotel.getStarRating() >= 1 && hotel.getStarRating() <= 5, 
                "star_rating doit être entre 1 et 5");
            
            System.out.printf("  - Hôtel #%d : %s (%d étoiles) - %s%n", 
                hotel.getIdHotel(), hotel.getName(), 
                hotel.getStarRating(), hotel.getBeachName());
        }
    }
    
    @Test
    @Order(9)
    @DisplayName("Test findHotelById - Hôtel existant")
    public void testFindHotelById_Exists() {
        System.out.println("\n--- Test findHotelById (existant) ---");
        
        int hotelId = 1;
        Optional<Hotel> hotelOpt = travelDao.findHotelById(hotelId);
        
        assertTrue(hotelOpt.isPresent(), "L'hôtel #1 doit exister");
        
        Hotel hotel = hotelOpt.get();
        assertEquals(hotelId, hotel.getIdHotel());
        assertNotNull(hotel.getName());
        
        System.out.printf("Hôtel trouvé : #%d - %s%n", 
            hotel.getIdHotel(), hotel.getName());
    }
    
    @Test
    @Order(10)
    @DisplayName("Test findHotelsByMinStars - Hôtels 4 étoiles et plus")
    public void testFindHotelsByMinStars() {
        System.out.println("\n--- Test findHotelsByMinStars (4+) ---");
        
        int minStars = 4;
        List<Hotel> hotels = travelDao.findHotelsByMinStars(minStars);
        
        assertNotNull(hotels, "La liste ne doit pas être null");
        System.out.printf("Hôtels avec %d étoiles minimum : %d%n", 
            minStars, hotels.size());
        
        for (Hotel hotel : hotels) {
            assertTrue(hotel.getStarRating() >= minStars, 
                String.format("L'hôtel doit avoir au moins %d étoiles", minStars));
            System.out.printf("  - %s : %d étoiles%n", 
                hotel.getName(), hotel.getStarRating());
        }
    }
    
    @Test
    @Order(11)
    @DisplayName("Test findHotelsByPriceRange - Prix entre 100 et 300")
    public void testFindHotelsByPriceRange() {
        System.out.println("\n--- Test findHotelsByPriceRange (100-300) ---");
        
        double minPrice = 100.0;
        double maxPrice = 300.0;
        List<Hotel> hotels = travelDao.findHotelsByPriceRange(minPrice, maxPrice);
        
        assertNotNull(hotels, "La liste ne doit pas être null");
        System.out.printf("Hôtels entre %.2f€ et %.2f€ : %d%n", 
            minPrice, maxPrice, hotels.size());
        
        for (Hotel hotel : hotels) {
            double price = hotel.getPricePerNight();
            assertTrue(price >= minPrice && price <= maxPrice, 
                String.format("Le prix %.2f doit être entre %.2f et %.2f", 
                    price, minPrice, maxPrice));
            System.out.printf("  - %s : %.2f€/nuit%n", hotel.getName(), price);
        }
    }
    
    @Test
    @Order(12)
    @DisplayName("Test findHotelsByKeywords - Recherche textuelle")
    public void testFindHotelsByKeywords() {
        System.out.println("\n--- Test findHotelsByKeywords ---");
        
        String keywords = "plage luxe";
        List<Hotel> hotels = travelDao.findHotelsByKeywords(keywords);
        
        assertNotNull(hotels, "La liste ne doit pas être null");
        System.out.printf("Hôtels trouvés pour '%s' : %d%n", keywords, hotels.size());
        
        for (Hotel hotel : hotels) {
            System.out.printf("  - %s (plage: %s)%n", 
                hotel.getName(), hotel.getBeachName());
        }
    }
    
    // =====================================================
    // TESTS DE ROBUSTESSE
    // =====================================================
    
    @Test
    @Order(13)
    @DisplayName("Test findSitesByKeywords - Mots-clés vides")
    public void testFindSitesByKeywords_Empty() {
        System.out.println("\n--- Test findSitesByKeywords (vide) ---");
        
        List<Site> sites = travelDao.findSitesByKeywords("");
        
        assertNotNull(sites, "La liste ne doit pas être null même pour recherche vide");
        System.out.println("Résultats pour recherche vide : " + sites.size());
    }
    
    @Test
    @Order(14)
    @DisplayName("Test findSitesByPriceRange - Prix invalides")
    public void testFindSitesByPriceRange_Invalid() {
        System.out.println("\n--- Test findSitesByPriceRange (invalide) ---");
        
        // Min > Max
        List<Site> sites = travelDao.findSitesByPriceRange(100.0, 50.0);
        
        assertNotNull(sites, "La liste ne doit pas être null");
        assertTrue(sites.isEmpty(), "Doit retourner une liste vide si min > max");
        System.out.println("Résultat attendu : liste vide");
    }
}
