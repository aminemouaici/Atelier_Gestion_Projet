package persistence.jdbc;

import business.domain.Site;
import business.domain.Hotel;
import java.util.List;

public class TestDao {

    public static void main(String[] args) {

        JdbcPersistence dao = new JdbcPersistence();

        System.out.println("=== ALL SITES ===");
        List<Site> sites = dao.getAllSites();
        System.out.println(sites);

        System.out.println("=== ALL HOTELS ===");
        List<Hotel> hotels = dao.getAllHotels();
        System.out.println(hotels);

        System.out.println("=== SITES BUDGET 0..50 ===");
        System.out.println(dao.findSitesByBudget(0, 50));

        System.out.println("=== HOTELS STARS >= 4 ===");
        System.out.println(dao.findHotelsByStars(4));
    }
}