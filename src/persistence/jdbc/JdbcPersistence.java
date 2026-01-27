package persistence.jdbc;

import API.JoinedOperator;
import business.domain.*;
import business.service.TravelDataAccess;
import dao.TravelDao;

import java.sql.ResultSet;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class JdbcPersistence implements TravelDao, TravelDataAccess {

    // Table T = Site, clé = id_site, répertoire R = src/lucene_docs (comme DataBase.sql)
    private static final String LUCENE_KEY_COL = "id_site";
    private static final String LUCENE_DOCS_DIR = "src/lucene_docs";

    // =========================================================
    // 1) HOTELS
    // =========================================================

    @Override
    public List<Hotel> getAllHotels() {
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
            System.err.println("SQL Exception in getAllHotels: " + e.getMessage());
        } finally {
            exec.close();
        }
        return hotels;
    }

    @Override
    public List<Hotel> findHotelsByStars(int minStars) {
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
            System.err.println("SQL Exception in findHotelsByStars: " + e.getMessage());
        } finally {
            exec.close();
        }
        return hotels;
    }

    @Override
    public List<Hotel> getHotelsByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return new ArrayList<>();

        String placeholders = ids.stream().map(x -> "?").collect(Collectors.joining(","));
        String query =
                "SELECT id_hotel, name, latitude, longitude, star_rating, beach_name, price_per_night " +
                "FROM Hotel " +
                "WHERE id_hotel IN (" + placeholders + ")";

        JdbcExecuteQuery exec = new JdbcExecuteQuery();
        exec.prepareQuery(query);

        List<Hotel> hotels = new ArrayList<>();
        try {
            for (int i = 0; i < ids.size(); i++) {
                exec.getPreparedStatement().setInt(i + 1, ids.get(i));
            }
            exec.sqlExecutePreparedQuery();

            ResultSet rs = exec.getResultSet();
            while (rs.next()) {
                hotels.add(mapHotel(rs));
            }
        } catch (Exception e) {
            System.err.println("SQL Exception in getHotelsByIds: " + e.getMessage());
        } finally {
            exec.close();
        }
        return hotels;
    }

    // ✅ MÉTHODE MANQUANTE AJOUTÉE
    @Override
    public List<Hotel> findHotelsByKeywords(String keywords) {
        // Recherche simple par nom ou description
        // Si vous voulez une recherche plus avancée, adaptez la requête
        String query =
                "SELECT id_hotel, name, latitude, longitude, star_rating, beach_name, price_per_night " +
                "FROM Hotel " +
                "WHERE name LIKE ? OR beach_name LIKE ? " +
                "ORDER BY star_rating DESC";

        JdbcExecuteQuery exec = new JdbcExecuteQuery();
        exec.prepareQuery(query);

        List<Hotel> hotels = new ArrayList<>();
        try {
            String searchPattern = "%" + (keywords != null ? keywords : "") + "%";
            exec.getPreparedStatement().setString(1, searchPattern);
            exec.getPreparedStatement().setString(2, searchPattern);
            
            exec.sqlExecutePreparedQuery();

            ResultSet rs = exec.getResultSet();
            while (rs.next()) {
                hotels.add(mapHotel(rs));
            }
        } catch (Exception e) {
            System.err.println("SQL Exception in findHotelsByKeywords: " + e.getMessage());
        } finally {
            exec.close();
        }
        return hotels;
    }

    // =========================================================
    // 2) SITES
    // =========================================================

    @Override
    public List<Site> getAllSites() {
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
            System.err.println("SQL Exception in getAllSites: " + e.getMessage());
        } finally {
            exec.close();
        }
        return sites;
    }

    @Override
    public List<Site> findSitesByBudget(double min, double max) {
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
            System.err.println("SQL Exception in findSitesByBudget: " + e.getMessage());
        } finally {
            exec.close();
        }
        return sites;
    }

    @Override
    public List<Site> getSitesByIds(List<Integer> ids) {
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
            System.err.println("SQL Exception in getSitesByIds: " + e.getMessage());
        } finally {
            exec.close();
        }

        // Preserve input order (important: order = score order)
        List<Site> ordered = new ArrayList<>();
        for (Integer id : ids) {
            Site s = byId.get(id);
            if (s != null) ordered.add(s);
        }
        return ordered;
    }

    // =========================================================
    // 3) RECHERCHE TEXTE (BDe : SQL + WITH Lucene)
    // =========================================================

    /**
     * Version BDA pure : id_site -> score (trié par pertinence décroissante)
     */
    @Override
    public Map<Integer, Double> findSiteIdsByKeywordsWithScore(String keywords) {
        // Requête mixte : partie SQL + "with ..."
        String mixedQuery = "SELECT id_site FROM Site with " + (keywords == null ? "" : keywords);

        JoinedOperator joined = new JoinedOperator(LUCENE_KEY_COL, LUCENE_DOCS_DIR);
        joined.init(mixedQuery);

        // LinkedHashMap ordonné par score desc (selon implémentation JoinedOperator)
        return joined.getResultJoined();
    }

    /**
     * Contrat business : retourne les objets Site.
     * On utilise Lucene pour ids+scores, puis on charge en SQL.
     */
    @Override
    public List<Site> findSitesByKeywords(String keywords) {
        Map<Integer, Double> scored = findSiteIdsByKeywordsWithScore(keywords);
        if (scored == null || scored.isEmpty()) return new ArrayList<>();

        List<Integer> orderedIds = new ArrayList<>(scored.keySet());
        return getSitesByIds(orderedIds);
    }

    // =========================================================
    // 4) MAPPING (DB -> Business Objects)
    // =========================================================

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