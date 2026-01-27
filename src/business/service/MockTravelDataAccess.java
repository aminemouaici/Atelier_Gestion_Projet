package business.service;

import business.domain.ActivitySite;
import business.domain.HistoricalSite;
import business.domain.Hotel;
import business.domain.Position;
import business.domain.Site;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation MOCK de TravelDataAccess
 * Utilisée pour tester la couche Business SANS la vraie base de données
 * 
 * ATTENTION : Cette classe est temporaire !
 * Elle sera remplacée par l'implémentation de l'équipe persistance.
 */
public class MockTravelDataAccess implements TravelDataAccess {
    
    // ==================== Données de test (en dur) ====================
    
    private List<Site> fakeSites;
    private List<Hotel> fakeHotels;
    
    // ==================== Constructeur ====================
    
    public MockTravelDataAccess() {
        initFakeSites();
        initFakeHotels();
    }
    
    /**
     * Initialise les sites de test
     */
    private void initFakeSites() {
        fakeSites = new ArrayList<>();
        
        // Sites d'activité (plongée, surf, etc.)
        fakeSites.add(new ActivitySite(
            1, 
            "Centre de plongée Aquatica", 
            45.0, 
            new Position(-17.535, -149.569),
            "Plongée sous-marine avec moniteur diplômé. Découvrez les fonds marins de Tahiti, coraux et poissons tropicaux.",
            LocalTime.of(8, 0),
            LocalTime.of(17, 0),
            Duration.ofHours(3)
        ));
        
        fakeSites.add(new ActivitySite(
            2, 
            "Lagon bleu snorkeling", 
            30.0, 
            new Position(-17.540, -149.575),
            "Snorkeling dans le lagon cristallin. Masque et tuba fournis. Idéal pour observer les raies et requins.",
            LocalTime.of(9, 0),
            LocalTime.of(16, 0),
            Duration.ofHours(2)
        ));
        
        fakeSites.add(new ActivitySite(
            3, 
            "École de surf Teahupoo", 
            60.0, 
            new Position(-17.850, -149.270),
            "Cours de surf pour tous niveaux. Spot mondialement connu pour ses vagues. Planche et combinaison incluses.",
            LocalTime.of(7, 0),
            LocalTime.of(18, 0),
            Duration.ofHours(4)
        ));
        
        fakeSites.add(new ActivitySite(
            4, 
            "Randonnée Mont Aorai", 
            25.0, 
            new Position(-17.580, -149.500),
            "Randonnée en montagne avec vue panoramique sur l'île. Guide inclus. Niveau intermédiaire.",
            LocalTime.of(6, 0),
            LocalTime.of(14, 0),
            Duration.ofHours(6)
        ));
        
        fakeSites.add(new ActivitySite(
            5, 
            "Jardin de corail", 
            35.0, 
            new Position(-17.545, -149.580),
            "Plongée dans un jardin de coraux préservé. Poissons multicolores garantis. Baptême de plongée possible.",
            LocalTime.of(8, 30),
            LocalTime.of(16, 30),
            Duration.ofHours(2)
        ));
        
        // Sites historiques
        fakeSites.add(new HistoricalSite(
            6, 
            "Musée de Tahiti et des Îles", 
            15.0, 
            new Position(-17.555, -149.590),
            "Musée retraçant l'histoire de la Polynésie française. Collections d'objets traditionnels et expositions temporaires.",
            LocalTime.of(9, 0),
            LocalTime.of(17, 0),
            Duration.ofHours(2)
        ));
        
        fakeSites.add(new HistoricalSite(
            7, 
            "Marae Arahurahu", 
            10.0, 
            new Position(-17.620, -149.580),
            "Ancien temple polynésien restauré. Site sacré avec cérémonies traditionnelles. Visite guidée incluse.",
            LocalTime.of(8, 0),
            LocalTime.of(18, 0),
            Duration.ofHours(1)
        ));
        
        fakeSites.add(new HistoricalSite(
            8, 
            "Cathédrale Notre-Dame de Papeete", 
            0.0, 
            new Position(-17.535, -149.568),
            "Cathédrale historique du centre-ville. Architecture coloniale. Entrée gratuite.",
            LocalTime.of(7, 0),
            LocalTime.of(19, 0),
            Duration.ofMinutes(45)
        ));
        
        fakeSites.add(new ActivitySite(
            9, 
            "Excursion dauphins", 
            80.0, 
            new Position(-17.530, -149.560),
            "Sortie en bateau pour observer les dauphins dans leur habitat naturel. Possibilité de nager avec eux.",
            LocalTime.of(7, 0),
            LocalTime.of(12, 0),
            Duration.ofHours(4)
        ));
        
        fakeSites.add(new ActivitySite(
            10, 
            "Parc naturel Te Faaiti", 
            20.0, 
            new Position(-17.600, -149.450),
            "Réserve naturelle avec cascades et végétation luxuriante. Sentiers balisés. Baignade possible.",
            LocalTime.of(6, 0),
            LocalTime.of(17, 0),
            Duration.ofHours(3)
        ));
    }
    
    /**
     * Initialise les hôtels de test
     */
    private void initFakeHotels() {
        fakeHotels = new ArrayList<>();
        
        fakeHotels.add(new Hotel(
            1, 
            "Tahiti Pearl Beach Resort", 
            150.0, 
            new Position(-17.535, -149.565),
            3, 
            "Plage de Punaauia"
        ));
        
        fakeHotels.add(new Hotel(
            2, 
            "InterContinental Tahiti Resort", 
            280.0, 
            new Position(-17.560, -149.600),
            4, 
            "Plage de Faa'a"
        ));
        
        fakeHotels.add(new Hotel(
            3, 
            "Le Méridien Tahiti", 
            350.0, 
            new Position(-17.540, -149.585),
            5, 
            "Plage privée Le Méridien"
        ));
        
        fakeHotels.add(new Hotel(
            4, 
            "Manava Suite Resort", 
            120.0, 
            new Position(-17.545, -149.570),
            3, 
            "Plage de Punaauia"
        ));
        
        fakeHotels.add(new Hotel(
            5, 
            "Hilton Tahiti Resort", 
            220.0, 
            new Position(-17.555, -149.595),
            4, 
            "Plage du Hilton"
        ));
        
        fakeHotels.add(new Hotel(
            6, 
            "Pension de la Plage", 
            60.0, 
            new Position(-17.550, -149.575),
            2, 
            "Plage publique Taharaa"
        ));
        
        fakeHotels.add(new Hotel(
            7, 
            "Sofitel Tahiti Ia Ora Beach Resort", 
            400.0, 
            new Position(-17.530, -149.555),
            5, 
            "Plage privée Sofitel"
        ));
    }
    
    // ==================== Implémentation de l'interface ====================
    
    @Override
    public List<Site> findSitesByKeywords(String keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return new ArrayList<>();
        }
        
        String searchLower = keywords.toLowerCase();
        
        return fakeSites.stream()
            .filter(site -> 
                site.getName().toLowerCase().contains(searchLower) ||
                site.getDescription().toLowerCase().contains(searchLower)
            )
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Site> findSitesByBudget(double min, double max) {
        return fakeSites.stream()
            .filter(site -> site.getPrice() >= min && site.getPrice() <= max)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Site> getAllSites() {
        return new ArrayList<>(fakeSites);
    }
    
    @Override
    public List<Hotel> findHotelsByKeywords(String keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return new ArrayList<>();
        }
        
        String searchLower = keywords.toLowerCase();
        
        return fakeHotels.stream()
            .filter(hotel -> 
                hotel.getName().toLowerCase().contains(searchLower) ||
                hotel.getBeachName().toLowerCase().contains(searchLower)
            )
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Hotel> findHotelsByStars(int minStars) {
        return fakeHotels.stream()
            .filter(hotel -> hotel.getStarRating() >= minStars)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Hotel> getAllHotels() {
        return new ArrayList<>(fakeHotels);
    }
}
