package dao;

import business.domain.Hotel;
import business.domain.Site;
import business.service.TravelDataAccess;

import java.util.List;
import java.util.Map;

/**
 * DAO côté persistance.
 * Étend l'interface métier TravelDataAccess.
 * Ajoute une méthode "BDA" qui expose les scores Lucene (utile pour la démo/rapport BDA).
 */
public interface TravelDao extends TravelDataAccess {

    /**
     * Version BDA pure : retourne les couples (id_site -> score Lucene)
     * triés par pertinence décroissante.
     */
    Map<Integer, Double> findSiteIdsByKeywordsWithScore(String keywords);

    /**
     * Chargement d'un lot de sites par ids (utilisé par la persistance).
     */
    List<Site> getSitesByIds(List<Integer> ids);

    /**
     * Chargement d'un lot d'hôtels par ids (si tu en as besoin plus tard).
     */
    List<Hotel> getHotelsByIds(List<Integer> ids);
}
