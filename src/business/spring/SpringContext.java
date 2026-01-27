package business.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Classe utilitaire pour accéder au contexte Spring
 * Singleton pour le chargement du contexte
 */
public class SpringContext {
    
    private static final String CONFIG_FILE = "business/spring/spring.xml";
    private static ApplicationContext context;
    
    // ==================== Méthodes statiques ====================
    
    /**
     * Retourne le contexte Spring (lazy loading)
     * @return le contexte applicatif Spring
     */
    public static ApplicationContext getContext() {
        if (context == null) {
            context = new ClassPathXmlApplicationContext(CONFIG_FILE);
        }
        return context;
    }
    
    /**
     * Récupère un bean par son nom
     * @param beanName le nom du bean
     * @return l'instance du bean
     */
    public static Object getBean(String beanName) {
        return getContext().getBean(beanName);
    }
    
    /**
     * Récupère un bean par son type
     * @param beanClass la classe du bean
     * @return l'instance du bean
     */
    public static <T> T getBean(Class<T> beanClass) {
        return getContext().getBean(beanClass);
    }
}
