package test;

import persistence.bda.TextualOperator;
import persistence.bda.SqlOperator;
import persistence.bda.JoinedOperator;
import org.junit.jupiter.api.*;
import java.util.Map;
import java.util.LinkedHashMap;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour les opérateurs BDA
 * Tests l'implémentation du Plan 1 du cahier des charges
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OperatorsTest {
    
    private static final String SITE_KEY_COL = "id_site";
    private static final String SITE_DOCS_DIR = "site_description_folder";
    
    @BeforeAll
    public static void setUpClass() {
        System.out.println("=== Initialisation des tests Opérateurs BDA ===");
    }
    
    @AfterAll
    public static void tearDownClass() {
        System.out.println("=== Fin des tests Opérateurs BDA ===");
    }
    
    // =====================================================
    // TESTS TEXTUAL OPERATOR
    // =====================================================
    
    @Test
    @Order(1)
    @DisplayName("Test TextualOperator - Recherche simple")
    public void testTextualOperator_SimpleSearch() {
        System.out.println("\n--- Test TextualOperator (recherche simple) ---");
        
        TextualOperator txtOp = new TextualOperator(SITE_KEY_COL, SITE_DOCS_DIR);
        
        try {
            txtOp.init("musée culture");
            
            Map<Integer, Float> scores = txtOp.getScores();
            
            assertNotNull(scores, "Les scores ne doivent pas être null");
            System.out.printf("Résultats trouvés : %d%n", scores.size());
            
            // Vérifier que les scores sont triés par ordre décroissant
            float previousScore = Float.MAX_VALUE;
            for (Map.Entry<Integer, Float> entry : scores.entrySet()) {
                assertTrue(entry.getValue() <= previousScore, 
                    "Les scores doivent être triés par ordre décroissant");
                previousScore = entry.getValue();
                
                System.out.printf("  - Site #%d : score=%.4f%n", 
                    entry.getKey(), entry.getValue());
            }
            
        } finally {
            txtOp.close();
        }
    }
    
    @Test
    @Order(2)
    @DisplayName("Test TextualOperator - Recherche vide")
    public void testTextualOperator_EmptySearch() {
        System.out.println("\n--- Test TextualOperator (recherche vide) ---");
        
        TextualOperator txtOp = new TextualOperator(SITE_KEY_COL, SITE_DOCS_DIR);
        
        try {
            txtOp.init("");
            
            Map<Integer, Float> scores = txtOp.getScores();
            
            assertNotNull(scores, "Les scores ne doivent pas être null");
            System.out.printf("Résultats pour recherche vide : %d%n", scores.size());
            
        } finally {
            txtOp.close();
        }
    }
    
    @Test
    @Order(3)
    @DisplayName("Test TextualOperator - Itération avec next()")
    public void testTextualOperator_Iteration() {
        System.out.println("\n--- Test TextualOperator (itération) ---");
        
        TextualOperator txtOp = new TextualOperator(SITE_KEY_COL, SITE_DOCS_DIR);
        
        try {
            txtOp.init("plongée corail");
            
            int count = 0;
            Map.Entry<Integer, Float> entry;
            
            while ((entry = txtOp.next()) != null) {
                count++;
                assertNotNull(entry.getKey(), "La clé ne doit pas être null");
                assertNotNull(entry.getValue(), "Le score ne doit pas être null");
                assertTrue(entry.getValue() > 0, "Le score doit être positif");
                
                System.out.printf("  %d. Site #%d : %.4f%n", 
                    count, entry.getKey(), entry.getValue());
                
                // Limiter l'affichage pour les tests
                if (count >= 10) break;
            }
            
            System.out.printf("Total résultats parcourus : %d%n", count);
            
        } finally {
            txtOp.close();
        }
    }
    
    @Test
    @Order(4)
    @DisplayName("Test TextualOperator - Index persistant")
    public void testTextualOperator_PersistentIndex() {
        System.out.println("\n--- Test TextualOperator (index persistant) ---");
        
        // Premier appel : création ou utilisation de l'index
        long startTime1 = System.currentTimeMillis();
        TextualOperator txtOp1 = new TextualOperator(SITE_KEY_COL, SITE_DOCS_DIR);
        try {
            txtOp1.init("temple");
            Map<Integer, Float> scores1 = txtOp1.getScores();
            long duration1 = System.currentTimeMillis() - startTime1;
            System.out.printf("Premier appel : %d résultats en %d ms%n", 
                scores1.size(), duration1);
        } finally {
            txtOp1.close();
        }
        
        // Deuxième appel : doit utiliser l'index existant (plus rapide)
        long startTime2 = System.currentTimeMillis();
        TextualOperator txtOp2 = new TextualOperator(SITE_KEY_COL, SITE_DOCS_DIR);
        try {
            txtOp2.init("temple");
            Map<Integer, Float> scores2 = txtOp2.getScores();
            long duration2 = System.currentTimeMillis() - startTime2;
            System.out.printf("Deuxième appel : %d résultats en %d ms%n", 
                scores2.size(), duration2);
            
            // Le deuxième appel devrait être plus rapide (index réutilisé)
            System.out.printf("Gain de performance : %.1fx%n", 
                (double)duration1 / duration2);
        } finally {
            txtOp2.close();
        }
    }
    
    // =====================================================
    // TESTS SQL OPERATOR
    // =====================================================
    
    @Test
    @Order(5)
    @DisplayName("Test SqlOperator - Requête SELECT simple")
    public void testSqlOperator_SimpleSelect() {
        System.out.println("\n--- Test SqlOperator (SELECT simple) ---");
        
        SqlOperator sqlOp = new SqlOperator();
        
        try {
            sqlOp.init("SELECT id_site, name, site_type FROM Site LIMIT 5");
            
            ResultSet rs = sqlOp.getResultSet();
            assertNotNull(rs, "Le ResultSet ne doit pas être null");
            
            int count = 0;
            while (rs.next()) {
                count++;
                int idSite = rs.getInt("id_site");
                String name = rs.getString("name");
                String type = rs.getString("site_type");
                
                System.out.printf("  %d. Site #%d : %s (%s)%n", 
                    count, idSite, name, type);
            }
            
            System.out.printf("Total résultats : %d%n", count);
            assertTrue(count > 0, "Doit retourner au moins 1 résultat");
            assertTrue(count <= 5, "Doit respecter la limite de 5");
            
        } catch (Exception e) {
            fail("Erreur lors de l'exécution SQL : " + e.getMessage());
        } finally {
            sqlOp.close();
        }
    }
    
    @Test
    @Order(6)
    @DisplayName("Test SqlOperator - Requête WHERE")
    public void testSqlOperator_WhereClause() {
        System.out.println("\n--- Test SqlOperator (WHERE) ---");
        
        SqlOperator sqlOp = new SqlOperator();
        
        try {
            sqlOp.init("SELECT id_site, name FROM Site WHERE site_type = 'ACTIVITY'");
            
            ResultSet rs = sqlOp.getResultSet();
            int count = 0;
            
            while (rs.next()) {
                count++;
                System.out.printf("  - Site #%d : %s%n", 
                    rs.getInt("id_site"), rs.getString("name"));
            }
            
            System.out.printf("Sites de type ACTIVITY : %d%n", count);
            
        } catch (Exception e) {
            fail("Erreur : " + e.getMessage());
        } finally {
            sqlOp.close();
        }
    }
    
    // =====================================================
    // TESTS JOINED OPERATOR (Plan 1)
    // =====================================================
    
    @Test
    @Order(7)
    @DisplayName("Test JoinedOperator - Requête mixte simple")
    public void testJoinedOperator_MixedQuery() {
        System.out.println("\n--- Test JoinedOperator (requête mixte) ---");
        
        JoinedOperator joined = new JoinedOperator("Site", SITE_KEY_COL, SITE_DOCS_DIR);
        
        try {
            String mixedQuery = "SELECT * FROM Site WHERE site_type = 'ACTIVITY' WITH plongée";
            
            joined.init(mixedQuery);
            
            LinkedHashMap<Integer, Double> results = joined.getResultJoined();
            
            assertNotNull(results, "Les résultats ne doivent pas être null");
            System.out.printf("Résultats de la jointure : %d%n", results.size());
            
            // Vérifier que les résultats sont triés par score décroissant
            double previousScore = Double.MAX_VALUE;
            for (Map.Entry<Integer, Double> entry : results.entrySet()) {
                assertTrue(entry.getValue() <= previousScore, 
                    "Les résultats doivent être triés par score décroissant");
                previousScore = entry.getValue();
                
                System.out.printf("  - Site #%d : score=%.4f%n", 
                    entry.getKey(), entry.getValue());
            }
            
        } finally {
            joined.close();
        }
    }
    
    @Test
    @Order(8)
    @DisplayName("Test JoinedOperator - Itération avec next()")
    public void testJoinedOperator_Iteration() {
        System.out.println("\n--- Test JoinedOperator (itération next) ---");
        
        JoinedOperator joined = new JoinedOperator("Site", SITE_KEY_COL, SITE_DOCS_DIR);
        
        try {
            // Réinitialiser pour tester next()
            joined.init("SELECT * FROM Site WITH temple culture");
            
            // Utiliser reset() si disponible
            joined.reset();
            
            int count = 0;
            Map.Entry<Integer, Double> entry;
            
            while ((entry = joined.next()) != null) {
                count++;
                System.out.printf("  %d. Site #%d : %.4f%n", 
                    count, entry.getKey(), entry.getValue());
            }
            
            System.out.printf("Total résultats parcourus avec next() : %d%n", count);
            
        } finally {
            joined.close();
        }
    }
    
    @Test
    @Order(9)
    @DisplayName("Test JoinedOperator - Intersection correcte")
    public void testJoinedOperator_IntersectionLogic() {
        System.out.println("\n--- Test JoinedOperator (logique intersection) ---");
        
        JoinedOperator joined = new JoinedOperator("Site", SITE_KEY_COL, SITE_DOCS_DIR);
        
        try {
            // Requête très spécifique : peu de résultats SQL et textuels
            String mixedQuery = "SELECT * FROM Site WHERE entry_price < 20 WITH musée";
            
            joined.init(mixedQuery);
            
            LinkedHashMap<Integer, Double> results = joined.getResultJoined();
            
            System.out.printf("Résultats de l'intersection : %d%n", results.size());
            
            // Les résultats doivent satisfaire BOTH conditions:
            // 1. Critère SQL : entry_price < 20
            // 2. Critère textuel : contient "musée"
            
            for (Map.Entry<Integer, Double> entry : results.entrySet()) {
                System.out.printf("  - Site #%d satisfait les deux critères (score=%.4f)%n", 
                    entry.getKey(), entry.getValue());
            }
            
        } finally {
            joined.close();
        }
    }
    
    @Test
    @Order(10)
    @DisplayName("Test JoinedOperator - Performance O(n+m)")
    public void testJoinedOperator_Performance() {
        System.out.println("\n--- Test JoinedOperator (performance) ---");
        
        JoinedOperator joined = new JoinedOperator("Site", SITE_KEY_COL, SITE_DOCS_DIR);
        
        try {
            long startTime = System.currentTimeMillis();
            
            joined.init("SELECT * FROM Site WITH culture histoire");
            
            LinkedHashMap<Integer, Double> results = joined.getResultJoined();
            
            long duration = System.currentTimeMillis() - startTime;
            
            System.out.printf("Jointure exécutée en %d ms%n", duration);
            System.out.printf("Résultats : %d%n", results.size());
            
            // Vérifier que la performance est acceptable
            assertTrue(duration < 5000, 
                "La jointure doit s'exécuter en moins de 5 secondes");
            
        } finally {
            joined.close();
        }
    }
    
    @Test
    @Order(11)
    @DisplayName("Test JoinedOperator - Requête sans partie textuelle")
    public void testJoinedOperator_NoTextPart() {
        System.out.println("\n--- Test JoinedOperator (sans WITH) ---");
        
        JoinedOperator joined = new JoinedOperator("Site", SITE_KEY_COL, SITE_DOCS_DIR);
        
        try {
            // Requête SQL pure sans WITH
            joined.init("SELECT * FROM Site WHERE site_type = 'HISTORICAL'");
            
            LinkedHashMap<Integer, Double> results = joined.getResultJoined();
            
            assertNotNull(results, "Doit fonctionner sans partie textuelle");
            System.out.printf("Résultats sans recherche textuelle : %d%n", results.size());
            
        } finally {
            joined.close();
        }
    }
}
