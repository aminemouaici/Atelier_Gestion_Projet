package persistence.jdbc;

import dao.TravelDao;
import business.domain.*;
import persistence.bda.JoinedOperator;

import java.sql.ResultSet;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implémentation JDBC de TravelDao.
 * Utilise JDBC pour SQL + Lucene pour recherche textuelle.
 * Respecte le Plan 1 du cahier des charges pour les requêtes mixtes.
 * 
 * @author Équipe Persistance
 */
public class JdbcTravelDao implements TravelDao {
    
    // Constantes BDA (conformes cahier des charges)
    private static final String SITE_KEY_COL = "id_site";
    private static final String SITE_DOCS_DIR = 
        "C:/Users/amine/eclipse-workspace/agpFinal/site_description_folder";
    
    // ==================== SITES ====================
    
    @Override
    public List<Site> findAllSites() {
        String query =
                "SELECT id_site, name, latitude, longitude, site_type, entry_price, " +
                "       start_time, end_time, visit_duration_minutes, short_description " +
                "FROM Site " +
                "ORDER BY id_site";
        
        JdbcExecuteQuery exec = new JdbcExecuteQuery();
        exec.prepareQuery(query);
        
        List<Site> sites = new ArrayList<>();
        try {
            exec.sqlExecutePreparedQuery();
            ResultSet rs = exec.getResultSet();
            while (rs.next()) {
                sites.add(mapSite(rs));
            }
        } catch (Exception e) {
            System.err.println("❌ SQL Exception in findAllSites: " + e.getMessage());
        } finally {
            exec.close();
        }
        return sites;
    }
    
    @Override
    public Optional<Site> findSiteById(int siteId) {
        String query =
                "SELECT id_site, name, latitude, longitude, site_type, entry_price, " +
                "       start_time, end_time, visit_duration_minutes, short_description " +
                "FROM Site " +
                "WHERE id_site = ?";
        
        JdbcExecuteQuery exec = new JdbcExecuteQuery();
        exec.prepareQuery(query);
        
        try {
            exec.getPreparedStatement().setInt(1, siteId);
            exec.sqlExecutePreparedQuery();
            
            ResultSet rs = exec.getResultSet();
            if (rs.next()) {
                return Optional.of(mapSite(rs));
            }
        } catch (Exception e) {
            System.err.println("❌ SQL Exception in findSiteById: " + e.getMessage());
        } finally {
            exec.close();
        }
        return Optional.empty();
    }
    
    @Override
    public List<Site> findSitesByKeywords(String keywords) {
        /**
         * REQUÊTE MIXTE - PLAN 1
         * 
         * Format : "SELECT ... FROM Site WITH mot-clés"
         * 
         * Exécution :
         * 1. TextOperator : Lucene sur répertoire R → Map<id_site, score>
         * 2. SqlOperator : SELECT * FROM Site → ResultSet
         * 3. JoinOperator : Jointure sur id_site
         * 4. Sort : Tri par score Lucene décroissant
         */
        
        String mixedQuery = "SELECT id_site FROM Site WITH " + 
                          (keywords == null ? "" : keywords);
        
        JoinedOperator joined = new JoinedOperator("Site", SITE_KEY_COL, SITE_DOCS_DIR);
        joined.init(mixedQuery);
        
        // Récupérer les IDs triés par score décroissant
        LinkedHashMap<Integer, Double> scored = joined.getResultJoined();
        
        if (scored == null || scored.isEmpty()) {
            joined.close();
            return new ArrayList<>();
        }
        
        List<Integer> orderedIds = new ArrayList<>(scored.keySet());
        
        // Charger les sites dans l'ordre du score
        List<Site> sites = getSitesByIds(orderedIds);
        
        joined.close();
        return sites;
    }
    
    @Override
    public List<Site> findSitesByType(String siteType) {
        String query =
                "SELECT id_site, name, latitude, longitude, site_type, entry_price, " +
                "       start_time, end_time, visit_duration_minutes, short_description " +
                "FROM Site " +
                "WHERE site_type = ? " +
                "ORDER BY name";
        
        JdbcExecuteQuery exec = new JdbcExecuteQuery();
        exec.prepareQuery(query);
        
        List<Site> sites = new ArrayList<>();
        try {
            exec.getPreparedStatement().setString(1, siteType);
            exec.sqlExecutePreparedQuery();
            
            ResultSet rs = exec.getResultSet();
            while (rs.next()) {
                sites.add(mapSite(rs));
            }
        } catch (Exception e) {
            System.err.println("❌ SQL Exception in findSitesByType: " + e.getMessage());
        } finally {
            exec.close();
        }
        return sites;
    }
    
    @Override
    public List<Site> findSitesByPriceRange(double min, double max) {
        String query =
                "SELECT id_site, name, latitude, longitude, site_type, entry_price, " +
                "       start_time, end_time, visit_duration_minutes, short_description " +
                "FROM Site " +
                "WHERE entry_price BETWEEN ? AND ? " +
                "ORDER BY entry_price ASC";
        
        JdbcExecuteQuery exec = new JdbcExecuteQuery();
        exec.prepareQuery(query);
        
        List<Site> sites = new ArrayList<>();
        try {
            exec.getPreparedStatement().setDouble(1, min);
            exec.getPreparedStatement().setDouble(2, max);
            exec.sqlExecutePreparedQuery();
            
            ResultSet rs = exec.getResultSet();
            while (rs.next()) {
                sites.add(mapSite(rs));
            }
        } catch (Exception e) {
            System.err.println("❌ SQL Exception in findSitesByPriceRange: " + e.getMessage());
        } finally {
            exec.close();
        }
        return sites;
    }
    
    // ==================== HOTELS ====================
    
    @Override
    public List<Hotel> findAllHotels() {
        String query =
                "SELECT id_hotel, name, latitude, longitude, star_rating, beach_name, price_per_night " +
                "FROM Hotel " +
                "ORDER BY id_hotel";
        
        JdbcExecuteQuery exec = new JdbcExecuteQuery();
        exec.prepareQuery(query);
        
        List<Hotel> hotels = new ArrayList<>();
        try {
            exec.sqlExecutePreparedQuery();
            ResultSet rs = exec.getResultSet();
            while (rs.next()) {
                hotels.add(mapHotel(rs));
            }
        } catch (Exception e) {
            System.err.println("❌ SQL Exception in findAllHotels: " + e.getMessage());
        } finally {
            exec.close();
        }
        return hotels;
    }
    
    @Override
    public Optional<Hotel> findHotelById(int hotelId) {
        String query =
                "SELECT id_hotel, name, latitude, longitude, star_rating, beach_name, price_per_night " +
                "FROM Hotel " +
                "WHERE id_hotel = ?";
        
        JdbcExecuteQuery exec = new JdbcExecuteQuery();
        exec.prepareQuery(query);
        
        try {
            exec.getPreparedStatement().setInt(1, hotelId);
            exec.sqlExecutePreparedQuery();
            
            ResultSet rs = exec.getResultSet();
            if (rs.next()) {
                return Optional.of(mapHotel(rs));
            }
        } catch (Exception e) {
            System.err.println("❌ SQL Exception in findHotelById: " + e.getMessage());
        } finally {
            exec.close();
        }
        return Optional.empty();
    }
    
    @Override
    public List<Hotel> findHotelsByMinStars(int minStars) {
        String query =
                "SELECT id_hotel, name, latitude, longitude, star_rating, beach_name, price_per_night " +
                "FROM Hotel " +
                "WHERE star_rating >= ? " +
                "ORDER BY star_rating DESC, price_per_night ASC";
        
        JdbcExecuteQuery exec = new JdbcExecuteQuery();
        exec.prepareQuery(query);
        
        List<Hotel> hotels = new ArrayList<>();
        try {
            exec.getPreparedStatement().setInt(1, minStars);
            exec.sqlExecutePreparedQuery();
            
            ResultSet rs = exec.getResultSet();
            while (rs.next()) {
                hotels.add(mapHotel(rs));
            }
        } catch (Exception e) {
            System.err.println("❌ SQL Exception in findHotelsByMinStars: " + e.getMessage());
        } finally {
            exec.close();
        }
        return hotels;
    }
    
    @Override
    public List<Hotel> findHotelsByPriceRange(double min, double max) {
        String query =
                "SELECT id_hotel, name, latitude, longitude, star_rating, beach_name, price_per_night " +
                "FROM Hotel " +
                "WHERE price_per_night BETWEEN ? AND ? " +
                "ORDER BY price_per_night ASC";
        
        JdbcExecuteQuery exec = new JdbcExecuteQuery();
        exec.prepareQuery(query);
        
        List<Hotel> hotels = new ArrayList<>();
        try {
            exec.getPreparedStatement().setDouble(1, min);
            exec.getPreparedStatement().setDouble(2, max);
            exec.sqlExecutePreparedQuery();
            
            ResultSet rs = exec.getResultSet();
            while (rs.next()) {
                hotels.add(mapHotel(rs));
            }
        } catch (Exception e) {
            System.err.println("❌ SQL Exception in findHotelsByPriceRange: " + e.getMessage());
        } finally {
            exec.close();
        }
        return hotels;
    }
    
    @Override
    public List<Hotel> findHotelsByKeywords(String keywords) {

        // 1) Nettoyer et découper les mots-clés
        String trimmed = (keywords == null) ? "" : keywords.trim();

        // Si l'utilisateur n'a rien tapé : on retourne tout (ou tu peux retourner liste vide si tu préfères)
        if (trimmed.isEmpty()) {
            String query =
                "SELECT id_hotel, name, price_per_night, latitude, longitude, star_rating, beach_name, description " +
                "FROM hotel " +
                "ORDER BY id_hotel";

            JdbcExecuteQuery exec = new JdbcExecuteQuery();
            exec.prepareQuery(query);

            List<Hotel> hotels = new ArrayList<>();
            try {
                exec.sqlExecutePreparedQuery();
                ResultSet rs = exec.getResultSet();
                while (rs.next()) {
                    hotels.add(mapHotel(rs));
                }
            } catch (Exception e) {
                System.err.println("❌ SQL Exception in findHotelsByKeywords (empty keywords): " + e.getMessage());
                e.printStackTrace();
            } finally {
                exec.close();
            }
            return hotels;
        }

        String[] words = trimmed.split("\\s+"); // séparation par espaces multiples

        // 2) Construire une requête avec OR sur chaque mot
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT id_hotel, name, price_per_night, latitude, longitude, star_rating, beach_name, description ")
          .append("FROM hotel ")
          .append("WHERE ");

        for (int i = 0; i < words.length; i++) {
            sb.append("description COLLATE utf8mb4_general_ci LIKE ?");
            if (i < words.length - 1) {
                sb.append(" OR ");
            }
        }

        sb.append(" ORDER BY id_hotel");

        String query = sb.toString();

        // 3) Préparer et binder les paramètres
        JdbcExecuteQuery exec = new JdbcExecuteQuery();
        exec.prepareQuery(query);

        List<Hotel> hotels = new ArrayList<>();
        try {
            for (int i = 0; i < words.length; i++) {
                exec.getPreparedStatement().setString(i + 1, "%" + words[i] + "%");
            }

            exec.sqlExecutePreparedQuery();

            ResultSet rs = exec.getResultSet();
            while (rs.next()) {
                hotels.add(mapHotel(rs));
            }

        } catch (Exception e) {
            System.err.println("❌ SQL Exception in findHotelsByKeywords: " + e.getMessage());
            e.printStackTrace();
        } finally {
            exec.close();
        }

        return hotels;
    }
    
    // ==================== MÉTHODES PRIVÉES ====================
    
    /**
     * Charge des sites par IDs en préservant l'ordre.
     * IMPORTANT : L'ordre des IDs = ordre de pertinence Lucene.
     */
    private List<Site> getSitesByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return new ArrayList<>();
        
        String placeholders = ids.stream().map(x -> "?").collect(Collectors.joining(","));
        String query =
                "SELECT id_site, name, latitude, longitude, site_type, entry_price, " +
                "       start_time, end_time, visit_duration_minutes, short_description " +
                "FROM Site " +
                "WHERE id_site IN (" + placeholders + ")";
        
        JdbcExecuteQuery exec = new JdbcExecuteQuery();
        exec.prepareQuery(query);
        
        Map<Integer, Site> byId = new HashMap<>();
        try {
            for (int i = 0; i < ids.size(); i++) {
                exec.getPreparedStatement().setInt(i + 1, ids.get(i));
            }
            exec.sqlExecutePreparedQuery();
            
            ResultSet rs = exec.getResultSet();
            while (rs.next()) {
                Site s = mapSite(rs);
                byId.put(s.getId(), s);
            }
        } catch (Exception e) {
            System.err.println("❌ SQL Exception in getSitesByIds: " + e.getMessage());
        } finally {
            exec.close();
        }
        
        // Préserver l'ordre d'entrée (= ordre Lucene)
        List<Site> ordered = new ArrayList<>();
        for (Integer id : ids) {
            Site s = byId.get(id);
            if (s != null) ordered.add(s);
        }
        return ordered;
    }
    
    /**
     * Mappe un ResultSet vers un Hotel.
     */
    private Hotel mapHotel(ResultSet rs) throws Exception {
        int id = rs.getInt("id_hotel");
        String name = rs.getString("name");
        double price = rs.getDouble("price_per_night");
        
        double lat = rs.getDouble("latitude");
        double lon = rs.getDouble("longitude");
        Position pos = new Position(lat, lon);
        
        int stars = rs.getInt("star_rating");
        String beach = rs.getString("beach_name");
        
        return new Hotel(id, name, price, pos, stars, beach);
    }
    
    /**
     * Mappe un ResultSet vers un Site (HistoricalSite ou ActivitySite).
     */
    private Site mapSite(ResultSet rs) throws Exception {
        int id = rs.getInt("id_site");
        String name = rs.getString("name");
        double price = rs.getDouble("entry_price");
        
        double lat = rs.getDouble("latitude");
        double lon = rs.getDouble("longitude");
        Position pos = new Position(lat, lon);
        
        String type = rs.getString("site_type"); // 'HISTORICAL' or 'ACTIVITY'
        
        Time st = rs.getTime("start_time");
        Time et = rs.getTime("end_time");
        LocalTime startTime = (st != null) ? st.toLocalTime() : LocalTime.of(8, 0);
        LocalTime endTime = (et != null) ? et.toLocalTime() : LocalTime.of(18, 0);
        
        int durMin = rs.getInt("visit_duration_minutes");
        Duration duration = Duration.ofMinutes(durMin);
        
        String desc = rs.getString("short_description");
        
        if ("HISTORICAL".equalsIgnoreCase(type)) {
            return new HistoricalSite(id, name, price, pos, desc, startTime, endTime, duration);
        }
        return new ActivitySite(id, name, price, pos, desc, startTime, endTime, duration);
    }
}
