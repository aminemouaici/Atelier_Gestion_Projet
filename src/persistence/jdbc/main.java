package persistence.jdbc;
//optionnel 

public class main {
    public static void main(String[] args) {
        JdbcPersistence p = new JdbcPersistence();

        System.out.println("=== HOTELS 4+ ===");
        System.out.println(p.findHotelsByStars(4));

        System.out.println("=== SITES budget 0..15 ===");
        System.out.println(p.findSitesByBudget(0, 15));

        System.out.println("=== SITES keywords (Lucene) : 'musée culture' ===");
        System.out.println(p.findSitesByKeywords("musée culture"));

        System.out.println("=== SCORES (BDA) : 'musée culture' ===");
        System.out.println(p.findSiteIdsByKeywordsWithScore("musée culture"));
    }
}
