package business.planning;

import business.domain.Hotel;
import business.domain.Site;
import business.simulation.Offer;
import business.simulation.OfferRequest;

import java.util.List;

/**
 * Interface pour le pattern Strategy
 * Définit le contrat pour les différentes stratégies de génération d'offres
 */
public interface OfferStrategy {
    
    /**
     * Construit une liste d'offres selon la stratégie implémentée
     * @param req les critères de recherche
     * @param sites la liste des sites disponibles
     * @param hotels la liste des hôtels disponibles
     * @return liste d'offres générées
     */
    List<Offer> buildOffers(OfferRequest req, List<Site> sites, List<Hotel> hotels);
}
