package persistence.bda;

/**
 * Interface générique pour les opérateurs de requêtes BDA.
 * Pattern Iterator pour parcourir les résultats.
 * 
 * @param <T> Type de résultat retourné par next()
 * @author Équipe Persistance
 */
public interface Operator<T> {
    
    /**
     * Initialise l'opérateur avec une requête.
     * 
     * @param query La requête à exécuter (SQL, texte, ou mixte)
     */
    void init(String query);
    
    /**
     * Retourne le prochain résultat (pattern Iterator).
     * 
     * @return Résultat suivant, ou null si terminé
     */
    T next();
    
    /**
     * Ferme les ressources utilisées (connexions, fichiers, etc.).
     */
    void close();
}
