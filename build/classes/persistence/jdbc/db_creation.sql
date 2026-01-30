-- =========================================================
-- SCRIPT COMPLET - BASE DE DONNÉES TAHITI TRAVEL
-- Création DB + Tables + Données complètes
-- Atelier de Gestion de Projet - Janvier 2026
-- =========================================================

-- =========================================================
-- ÉTAPE 1 : CRÉATION DE LA BASE DE DONNÉES
-- =========================================================

-- Supprimer la base si elle existe déjà
DROP DATABASE IF EXISTS tahiti_travel;

-- Créer la base de données avec charset UTF-8
CREATE DATABASE tahiti_travel 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_general_ci;

-- Sélectionner la base de données
USE tahiti_travel;

-- =========================================================
-- ÉTAPE 2 : CRÉATION DES TABLES
-- =========================================================

-- Nettoyage si tables existent déjà 
DROP TABLE IF EXISTS Transport_Route;
DROP TABLE IF EXISTS Site_Transport;
DROP TABLE IF EXISTS Site;
DROP TABLE IF EXISTS Hotel;
DROP TABLE IF EXISTS Transport_Mode;

-- =========================================================
-- TABLE 1 : TRANSPORT_MODE
-- Modes de transport disponibles avec caractéristiques
-- =========================================================
CREATE TABLE Transport_Mode (
    id_transport INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE, -- 'FOOT', 'BUS', 'BOAT'
    comfort_score INT NOT NULL CHECK (comfort_score BETWEEN 0 AND 100),
    avg_speed_kmh DOUBLE NOT NULL CHECK (avg_speed_kmh > 0),
    price_per_km DOUBLE NOT NULL CHECK (price_per_km >= 0),
    is_active BOOLEAN DEFAULT TRUE,
    
    INDEX idx_transport_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- =========================================================
-- TABLE 2 : HOTEL
-- Hôtels au bord de mer (chaque hôtel a une plage)
-- =========================================================
CREATE TABLE Hotel (
    id_hotel INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(200),
    phone_number VARCHAR(20),
    
    -- Localisation géographique
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    
    -- Caractéristiques hôtel
    star_rating INT NOT NULL CHECK (star_rating BETWEEN 1 AND 5),
    beach_name VARCHAR(100) NOT NULL,
    price_per_night DOUBLE NOT NULL CHECK (price_per_night > 0),
    
    -- Informations supplémentaires
    description TEXT,
    total_rooms INT,
    
    INDEX idx_hotel_stars (star_rating),
    INDEX idx_hotel_price (price_per_night),
    INDEX idx_hotel_location (latitude, longitude)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- =========================================================
-- TABLE 3 : SITE 
-- Sites touristiques (historiques ou activités)
-- NOTE : Les descriptions textuelles complètes sont dans les fichiers textuelles
-- =========================================================
CREATE TABLE Site (
    id_site INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    address VARCHAR(200),
    
    -- Localisation
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL,
    
    -- Type de site
    site_type ENUM('HISTORICAL', 'ACTIVITY') NOT NULL,
    activity_category VARCHAR(50), -- Ex: 'plongée', 'randonnée', 'surf', 'culture'
    
    -- Tarifs et horaires
    entry_price DOUBLE DEFAULT 0.0 CHECK (entry_price >= 0),
    start_time TIME DEFAULT '08:00:00',
    end_time TIME DEFAULT '18:00:00',
    visit_duration_minutes INT DEFAULT 120,
    
    -- Description courte (BD relationnelle)
    short_description VARCHAR(500),
    
    has_full_description BOOLEAN DEFAULT FALSE,
    
    INDEX idx_site_type (site_type),
    INDEX idx_site_category (activity_category),
    INDEX idx_site_price (entry_price),
    INDEX idx_site_location (latitude, longitude)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- =========================================================
-- TABLE 4 : SITE_TRANSPORT
-- Quels modes de transport peuvent accéder à chaque site
-- =========================================================
CREATE TABLE Site_Transport (
    id_site INT NOT NULL,
    id_transport INT NOT NULL,
    
    PRIMARY KEY (id_site, id_transport),
    
    FOREIGN KEY (id_site) REFERENCES Site(id_site) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_transport) REFERENCES Transport_Mode(id_transport)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- =========================================================
-- TABLE 5 : TRANSPORT_ROUTE 
-- Distances prédéfinies entre structures (optimisation)
-- =========================================================
CREATE TABLE Transport_Route (
    id_route INT PRIMARY KEY AUTO_INCREMENT,
    
    -- Origine et destination (polymorphe)
    origin_type ENUM('HOTEL', 'SITE') NOT NULL,
    origin_id INT NOT NULL,
    
    destination_type ENUM('HOTEL', 'SITE') NOT NULL,
    destination_id INT NOT NULL,
    
    -- Distance et transport recommandé
    distance_km DOUBLE NOT NULL CHECK (distance_km >= 0),
    recommended_transport_id INT,
    estimated_duration_minutes INT,
    
    -- Contraintes
    UNIQUE KEY unique_route (origin_type, origin_id, destination_type, destination_id),
    
    FOREIGN KEY (recommended_transport_id) REFERENCES Transport_Mode(id_transport)
        ON DELETE SET NULL,
        
    INDEX idx_route_origin (origin_type, origin_id),
    INDEX idx_route_destination (destination_type, destination_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- =========================================================
-- ÉTAPE 3 : INSERTION DES DONNÉES
-- =========================================================

-- =========================================================
-- DONNÉES : TRANSPORT_MODE (3 modes)
-- =========================================================
INSERT INTO Transport_Mode (name, comfort_score, avg_speed_kmh, price_per_km) VALUES
('FOOT', 30, 5.0, 0.0),      -- Marche : gratuit, lent, peu confortable
('BUS', 60, 40.0, 0.50),     -- Bus : moyen confort, prix modéré
('BOAT', 80, 30.0, 1.20);    -- Bateau : confortable, plus cher

-- =========================================================
-- DONNÉES : HOTEL (15 hôtels )
-- =========================================================

-- === HÔTELS INITIAUX (5 hôtels) ===
INSERT INTO Hotel (name, address, phone_number, latitude, longitude, star_rating, beach_name, price_per_night, description, total_rooms) VALUES
('Le Méridien Tahiti', 'Punaauia, Tahiti', '+689 40 47 07 07', -17.5863, -149.6099, 5, 'Plage de Punaauia', 350.00, 'Hôtel de luxe avec vue sur Moorea', 150),
('InterContinental Tahiti Resort', 'Faaa, Tahiti', '+689 40 86 51 10', -17.5466, -149.6086, 5, 'Plage de Faaa', 400.00, 'Resort international avec bungalows sur pilotis', 200),
('Manava Suite Resort', 'Punaauia, Tahiti', '+689 40 47 31 00', -17.5925, -149.6142, 4, 'Plage Manava', 250.00, 'Hôtel familial au bord du lagon', 120),
('Tahiti Pearl Beach Resort', 'Arue, Tahiti', '+689 40 50 10 10', -17.5234, -149.5012, 4, 'Plage Arue', 280.00, 'Vue panoramique sur la baie de Matavai', 140),
('Hotel Tiare Tahiti', 'Papeete Centre', '+689 40 50 01 00', -17.5350, -149.5699, 3, 'Plage Urbaine', 150.00, 'Hôtel économique en ville', 80);

-- === HÔTELS ENRICHIS (10 hôtels supplémentaires) ===

-- CATÉGORIE BUDGET (1-2 étoiles, 80-150€)
INSERT INTO Hotel (name, address, phone_number, latitude, longitude, star_rating, beach_name, price_per_night, description, total_rooms) VALUES
('Pension Fare Suisse', 'Fare Tony, PK 11', '+689 40 42 00 30', -17.5712, -149.6234, 2, 'Plage Lafayette', 85.00, 'Pension familiale authentique avec petit-déjeuner polynésien inclus', 12),
('Chez Myrna', 'Mahina, PK 4.5', '+689 40 48 25 79', -17.5123, -149.4856, 2, 'Plage Taharu''u', 95.00, 'Bungalows traditionnels dans jardin tropical, ambiance locale', 8),
('Fare Vai Moana', 'Paea, PK 24', '+689 40 57 41 41', -17.6834, -149.5678, 2, 'Plage Paea', 110.00, 'Vue lagon, idéal familles, kitchenette équipée', 15),
('Relais Fenua', 'Punaauia, PK 18', '+689 40 58 48 00', -17.6145, -149.6345, 3, 'Plage Vaiava', 135.00, 'Hôtel récent, proche commerces et restaurants', 25);

-- CATÉGORIE MOYENNE (3-4 étoiles, 150-280€)
INSERT INTO Hotel (name, address, phone_number, latitude, longitude, star_rating, beach_name, price_per_night, description, total_rooms) VALUES
('Royal Tahitien', 'Pirae, Motu Uta', '+689 40 50 40 40', -17.5289, -149.5234, 3, 'Plage Arue Est', 165.00, 'Hôtel sur îlot privé, navette gratuite vers centre-ville', 45),
('Le Tahiti by Pearl Resorts', 'Arue, Lafayette', '+689 40 86 66 66', -17.5198, -149.5145, 4, 'Plage Lafayette', 195.00, 'Design moderne, piscine infinity, spa polynésien', 90),
('Vanira Lodge', 'Mahina, PK 9', '+689 40 48 51 62', -17.5456, -149.4923, 3, 'Plage Mahina', 175.00, 'Écolodge avec bungalows en bois, snorkeling devant hôtel', 18),
('Tahiti Airport Motel', 'Faaa, près aéroport', '+689 40 82 23 23', -17.5567, -149.6178, 3, 'Plage Aéroport', 145.00, 'Pratique transit, chambres climatisées, navette aéroport', 35);

-- CATÉGORIE LUXE (4-5 étoiles, 280-450€)
INSERT INTO Hotel (name, address, phone_number, latitude, longitude, star_rating, beach_name, price_per_night, description, total_rooms) VALUES
('Sofitel Tahiti Maeva Beach', 'Punaauia, PK 7', '+689 40 86 66 00', -17.5845, -149.6089, 4, 'Plage Maeva', 320.00, 'Resort moderne avec vue Moorea, 3 restaurants gastronomiques', 215),
('Radisson Plaza Tahiti', 'Arue, Baie Matavai', '+689 40 48 88 88', -17.5201, -149.5034, 4, 'Plage Matavai', 295.00, 'Hôtel historique rénové, plage de sable noir volcanique', 162),
('Te Moana Tahiti Resort', 'Punaauia, PK 15', '+689 40 50 62 00', -17.6067, -149.6267, 5, 'Plage Te Moana', 380.00, 'Bungalows sur pilotis, centre de plongée PADI, excursions dauphins', 78);

-- =========================================================
-- DONNÉES : SITE (25 sites touristiques)
-- =========================================================

-- === SITES INITIAUX (10 sites) ===
INSERT INTO Site (name, address, latitude, longitude, site_type, activity_category, entry_price, visit_duration_minutes, short_description, has_full_description) VALUES
-- Sites historiques
('Musée de Tahiti et des Îles', 'Punaauia, PK 15', -17.6099, -149.6285, 'HISTORICAL', 'culture', 8.00, 120, 'Musée ethnographique sur la culture polynésienne', TRUE),
('Marae Arahurahu', 'Paea, PK 22.5', -17.6712, -149.5932, 'HISTORICAL', 'culture', 0.00, 90, 'Temple polynésien restauré du 17e siècle', TRUE),
('Cathédrale Notre-Dame', 'Papeete Centre', -17.5354, -149.5693, 'HISTORICAL', 'culture', 0.00, 45, 'Cathédrale historique de 1875', TRUE),
('Marché de Papeete', 'Front de mer, Papeete', -17.5334, -149.5662, 'HISTORICAL', 'culture', 0.00, 150, 'Marché traditionnel et artisanat local', TRUE),

-- Sites d'activités
('Plongée Lagon Bleu', 'Faaa, PK 7', -17.5512, -149.5989, 'ACTIVITY', 'plongée', 75.00, 180, 'Spot de plongée avec raies et requins', TRUE),
('Cascade de Fautaua', 'Vallée de Fautaua', -17.5689, -149.5234, 'ACTIVITY', 'randonnée', 5.00, 240, 'Randonnée vers une cascade de 300m', TRUE),
('Surf Spot Taapuna', 'Punaauia, Plage Taapuna', -17.5912, -149.6178, 'ACTIVITY', 'surf', 0.00, 120, 'Vagues pour surfeurs intermédiaires', TRUE),
('Jardin Botanique Harrison Smith', 'Papeari, PK 51.2', -17.7334, -149.4512, 'ACTIVITY', 'nature', 6.00, 90, 'Jardin tropical avec espèces rares', TRUE),
('Parc Vaipahi', 'Mataiea, PK 48.5', -17.7123, -149.4789, 'ACTIVITY', 'nature', 0.00, 75, 'Parc avec cascades et bassins naturels', TRUE),
('Centre Nautique Tahiti', 'Marina Taina', -17.5945, -149.6234, 'ACTIVITY', 'nautique', 55.00, 150, 'Jet-ski, paddle, kayak de mer', TRUE);

-- === SITES ENRICHIS (15 sites supplémentaires) ===

-- SITES HISTORIQUES (6 nouveaux)
INSERT INTO Site (name, address, latitude, longitude, site_type, activity_category, entry_price, start_time, end_time, visit_duration_minutes, short_description, has_full_description) VALUES
('Tombeau du Roi Pomare V', 'Arue, Pointe Vénus', -17.4956, -149.4812, 'HISTORICAL', 'culture', 0.00, '07:00:00', '18:00:00', 45, 'Monument funéraire du dernier roi de Tahiti (1891)', TRUE),
('Musée de la Perle Robert Wan', 'Papeete, Front de mer', -17.5378, -149.5689, 'HISTORICAL', 'culture', 10.00, '09:00:00', '17:00:00', 90, 'Exposition sur la perliculture polynésienne', TRUE),
('Grotte de Maraa', 'Paea, PK 28.5', -17.7012, -149.5834, 'HISTORICAL', 'nature', 0.00, '08:00:00', '17:00:00', 60, 'Grotte sacrée avec bassins naturels et légendes polynésiennes', TRUE),
('Marae Mahaiatea', 'Papara, PK 39', -17.7456, -149.5123, 'HISTORICAL', 'culture', 0.00, '08:00:00', '18:00:00', 75, 'Ruines du plus grand marae de Polynésie (17e siècle)', TRUE),
('Point Vénus', 'Mahina, Pointe Vénus', -17.4934, -149.4789, 'HISTORICAL', 'nature', 0.00, '06:00:00', '20:00:00', 120, 'Phare historique, plage de sable noir, lieu débarquement Cook', TRUE),
('Galerie Winkler', 'Papeete, Rue Jeanne d''Arc', -17.5389, -149.5712, 'HISTORICAL', 'culture', 0.00, '09:00:00', '17:00:00', 60, 'Galerie d''art avec oeuvres d''artistes polynésiens', TRUE);

-- SITES D'ACTIVITÉS NATURE (5 nouveaux)
INSERT INTO Site (name, address, latitude, longitude, site_type, activity_category, entry_price, start_time, end_time, visit_duration_minutes, short_description, has_full_description) VALUES
('Belvédère de Tahiti', 'Vallée Papenoo, Route relais', -17.5834, -149.5145, 'ACTIVITY', 'randonnée', 0.00, '06:00:00', '18:00:00', 180, 'Point de vue panoramique 600m altitude, randonnée 4x4', TRUE),
('Cascade des Trois Bassins', 'Faaone, PK 47', -17.7234, -149.4567, 'ACTIVITY', 'nature', 0.00, '08:00:00', '17:00:00', 150, 'Randonnée facile vers 3 cascades et piscines naturelles', TRUE),
('Trou du Souffleur', 'Tiarei, côte Est', -17.5345, -149.3456, 'ACTIVITY', 'nature', 0.00, '08:00:00', '17:00:00', 30, 'Phénomène naturel : jet d''eau de 15m lors des vagues', TRUE),
('Vallée de Papenoo', 'Papenoo, Route intérieure', -17.5678, -149.4234, 'ACTIVITY', 'randonnée', 45.00, '08:00:00', '16:00:00', 360, 'Safari 4x4 dans vallée luxuriante, cascades et sites archéologiques', TRUE),
('Jardin d''Eau de Vaipahi', 'Mataiea, PK 49', -17.7145, -149.4712, 'ACTIVITY', 'nature', 5.00, '08:00:00', '17:00:00', 90, 'Jardin botanique avec étangs, cascade et sentiers ombragés', TRUE);

-- SITES D'ACTIVITÉS AQUATIQUES (4 nouveaux)
INSERT INTO Site (name, address, latitude, longitude, site_type, activity_category, entry_price, start_time, end_time, visit_duration_minutes, short_description, has_full_description) VALUES
('Centre de Plongée Eleuthera', 'Punaauia, PK 16', -17.6089, -149.6234, 'ACTIVITY', 'plongée', 85.00, '08:00:00', '17:00:00', 180, 'Baptême plongée et exploration épaves, raies léopards', TRUE),
('Spot de Snorkeling Motu', 'Punaauia, Motu Martin', -17.5923, -149.6345, 'ACTIVITY', 'plongée', 35.00, '09:00:00', '16:00:00', 120, 'Excursion motu avec snorkeling, coraux multicolores et tortues', TRUE),
('École de Surf Taapuna', 'Punaauia, Plage Taapuna', -17.5934, -149.6189, 'ACTIVITY', 'surf', 40.00, '07:00:00', '18:00:00', 120, 'Cours de surf débutants à confirmés, planches fournies', TRUE),
('Lagoonarium Tahiti', 'Punaauia, PK 11.4', -17.5889, -149.6123, 'ACTIVITY', 'plongée', 55.00, '09:00:00', '17:00:00', 180, 'Aquarium naturel, rencontre requins et raies en enclos sécurisé', TRUE);

INSERT INTO Site (name, address, latitude, longitude, site_type, activity_category, entry_price, start_time, end_time, visit_duration_minutes, short_description, has_full_description) VALUES
('Kayak Polynésien', 'Arue, Marina Lafayette', -17.5167, -149.5089, 'ACTIVITY', 'nautique', 45.00, '08:00:00', '17:00:00', 150, 'Location kayak et stand-up paddle, tour guidé du lagon', TRUE);

-- =========================================================
-- DONNÉES : SITE_TRANSPORT
-- =========================================================

-- Sites initiaux (1-10)
INSERT INTO Site_Transport (id_site, id_transport) VALUES
-- Musée de Tahiti : bus ou à pied
(1, 1), (1, 2),
-- Marae : bus uniquement (éloigné)
(2, 2),
-- Cathédrale : tous transports (centre-ville)
(3, 1), (3, 2),
-- Marché : tous (centre-ville)
(4, 1), (4, 2),
-- Plongée : bateau + bus
(5, 2), (5, 3),
-- Cascade : bus + marche obligatoire
(6, 1), (6, 2),
-- Surf : bus ou à pied
(7, 1), (7, 2),
-- Jardin botanique : bus
(8, 2),
-- Parc Vaipahi : bus
(9, 2),
-- Centre nautique : bus ou bateau
(10, 2), (10, 3);


INSERT INTO Site_Transport (id_site, id_transport) VALUES
-- Tombeau Pomare V (11) : à pied ou bus
(11, 1), (11, 2),
-- Musée Perle (12) : tous transports (centre)
(12, 1), (12, 2),
-- Grotte Maraa (13) : bus uniquement
(13, 2),
-- Marae Mahaiatea (14) : bus uniquement
(14, 2),
-- Point Vénus (15) : bus ou à pied
(15, 1), (15, 2),
-- Galerie Winkler (16) : tous (centre)
(16, 1), (16, 2),
-- Belvédère (17) : 4x4 obligatoire (= bus dans notre système)
(17, 2),
-- Cascade Trois Bassins (18) : bus puis marche
(18, 1), (18, 2),
-- Trou Souffleur (19) : bus
(19, 2),
-- Vallée Papenoo (20) : 4x4 (= bus)
(20, 2),
-- Jardin Vaipahi (21) : bus
(21, 2),
-- Centre Plongée Eleuthera (22) : bus ou bateau
(22, 2), (22, 3),
-- Snorkeling Motu (23) : bateau obligatoire
(23, 3),
-- École Surf (24) : bus ou à pied
(24, 1), (24, 2),
-- Lagoonarium (25) : bus ou bateau
(25, 2), (25, 3),
-- Kayak Polynésien (26) : bus ou à pied - NOTE: Le site 26 n'existe que si vous avez 26 sites
(26, 1), (26, 2);

-- =========================================================
-- DONNÉES : TRANSPORT_ROUTE
-- =========================================================

INSERT INTO Transport_Route (origin_type, origin_id, destination_type, destination_id, distance_km, recommended_transport_id, estimated_duration_minutes) VALUES
-- Du Le Méridien vers sites
('HOTEL', 1, 'SITE', 1, 2.5, 2, 10),   -- vers Musée
('HOTEL', 1, 'SITE', 7, 0.8, 1, 15),   -- vers Surf Spot
('HOTEL', 1, 'SITE', 5, 3.2, 3, 20),   -- vers Plongée (bateau)

-- Entre sites
('SITE', 1, 'SITE', 2, 8.5, 2, 25),    -- Musée → Marae
('SITE', 3, 'SITE', 4, 0.3, 1, 5);     -- Cathédrale → Marché

INSERT INTO Transport_Route (origin_type, origin_id, destination_type, destination_id, distance_km, recommended_transport_id, estimated_duration_minutes) VALUES
-- DU LE MÉRIDIEN (id_hotel=1)
('HOTEL', 1, 'SITE', 2, 8.2, 2, 20),    -- vers Marae Arahurahu
('HOTEL', 1, 'SITE', 3, 5.1, 2, 15),    -- vers Cathédrale
('HOTEL', 1, 'SITE', 4, 5.4, 2, 15),    -- vers Marché
('HOTEL', 1, 'SITE', 6, 15.3, 2, 35),   -- vers Cascade Fautaua
('HOTEL', 1, 'SITE', 8, 35.8, 2, 65),   -- vers Jardin Harrison Smith
('HOTEL', 1, 'SITE', 10, 1.2, 1, 20),   -- vers Centre Nautique

-- DE L'INTERCONTINENTAL (id_hotel=2)
('HOTEL', 2, 'SITE', 1, 3.8, 2, 15),    -- vers Musée
('HOTEL', 2, 'SITE', 3, 8.7, 2, 25),    -- vers Cathédrale
('HOTEL', 2, 'SITE', 4, 9.1, 2, 25),    -- vers Marché
('HOTEL', 2, 'SITE', 5, 1.5, 1, 10),    -- vers Plongée Lagon Bleu
('HOTEL', 2, 'SITE', 7, 4.2, 2, 15),    -- vers Surf Taapuna
('HOTEL', 2, 'SITE', 10, 4.8, 2, 15),   -- vers Centre Nautique

-- DU MANAVA (id_hotel=3)
('HOTEL', 3, 'SITE', 1, 1.8, 1, 15),    -- vers Musée
('HOTEL', 3, 'SITE', 2, 6.5, 2, 18),    -- vers Marae
('HOTEL', 3, 'SITE', 7, 0.5, 1, 10),    -- vers Surf Taapuna
('HOTEL', 3, 'SITE', 10, 2.1, 1, 15),   -- vers Centre Nautique

-- DU TAHITI PEARL BEACH (id_hotel=4)
('HOTEL', 4, 'SITE', 3, 1.2, 1, 10),    -- vers Cathédrale
('HOTEL', 4, 'SITE', 4, 0.8, 1, 8),     -- vers Marché
('HOTEL', 4, 'SITE', 6, 12.5, 2, 30),   -- vers Cascade
('HOTEL', 4, 'SITE', 11, 0.3, 1, 5),    -- vers Tombeau Pomare V

-- DU TIARE TAHITI (id_hotel=5)
('HOTEL', 5, 'SITE', 3, 0.4, 1, 5),     -- vers Cathédrale
('HOTEL', 5, 'SITE', 4, 0.2, 1, 3),     -- vers Marché
('HOTEL', 5, 'SITE', 12, 0.6, 1, 8),    -- vers Musée Perle

-- DU PENSION FARE SUISSE (id_hotel=6)
('HOTEL', 6, 'SITE', 1, 3.5, 2, 12),    -- vers Musée
('HOTEL', 6, 'SITE', 7, 2.8, 1, 20),    -- vers Surf
('HOTEL', 6, 'SITE', 10, 4.2, 2, 15),   -- vers Centre Nautique

-- DU CHEZ MYRNA (id_hotel=7)
('HOTEL', 7, 'SITE', 11, 1.8, 1, 12),   -- vers Tombeau Pomare
('HOTEL', 7, 'SITE', 15, 0.5, 1, 8),    -- vers Point Vénus
('HOTEL', 7, 'SITE', 4, 8.2, 2, 22),    -- vers Marché

-- DU FARE VAI MOANA (id_hotel=8)
('HOTEL', 8, 'SITE', 2, 2.1, 1, 15),    -- vers Marae
('HOTEL', 8, 'SITE', 13, 3.5, 2, 12),   -- vers Grotte Maraa
('HOTEL', 8, 'SITE', 9, 24.8, 2, 45),   -- vers Parc Vaipahi

-- DU SOFITEL MAEVA (id_hotel=9)
('HOTEL', 9, 'SITE', 5, 2.3, 2, 10),    -- vers Plongée
('HOTEL', 9, 'SITE', 7, 1.5, 1, 12),    -- vers Surf
('HOTEL', 9, 'SITE', 22, 4.8, 2, 15),   -- vers Lagoonarium

-- DU RADISSON PLAZA (id_hotel=10)
('HOTEL', 10, 'SITE', 11, 0.8, 1, 8),   -- vers Tombeau Pomare
('HOTEL', 10, 'SITE', 15, 1.2, 1, 10),  -- vers Point Vénus
('HOTEL', 10, 'SITE', 3, 1.5, 1, 12),   -- vers Cathédrale

-- DU TE MOANA RESORT (id_hotel=11)
('HOTEL', 11, 'SITE', 1, 0.8, 1, 10),   -- vers Musée
('HOTEL', 11, 'SITE', 19, 1.5, 1, 12),  -- vers Centre Plongée Eleuthera
('HOTEL', 11, 'SITE', 20, 2.1, 2, 12),  -- vers Snorkeling Motu

-- ENTRE SITES (liaisons utiles)
('SITE', 1, 'SITE', 8, 33.5, 2, 55),    -- Musée → Jardin Harrison
('SITE', 2, 'SITE', 13, 5.2, 2, 15),    -- Marae → Grotte Maraa
('SITE', 3, 'SITE', 12, 0.5, 1, 5),     -- Cathédrale → Musée Perle
('SITE', 5, 'SITE', 19, 5.2, 2, 18),    -- Plongée Lagon → Eleuthera
('SITE', 7, 'SITE', 21, 0.8, 1, 10),    -- Surf Taapuna → École Surf
('SITE', 11, 'SITE', 15, 0.9, 1, 10),   -- Tombeau → Point Vénus
('SITE', 13, 'SITE', 14, 10.8, 2, 25),  -- Grotte → Marae Mahaiatea
('SITE', 17, 'SITE', 18, 2.5, 1, 20);   -- Belvédère → Cascade Trois Bassins

-- =========================================================
-- ÉTAPE 4 : CRÉATION DES VUES
-- =========================================================

-- Supprimer vues si elles existent déjà
DROP VIEW IF EXISTS v_site_with_transports;
DROP VIEW IF EXISTS v_hotel_summary;

-- Vue : Sites avec leurs transports disponibles
CREATE VIEW v_site_with_transports AS
SELECT 
    s.id_site,
    s.name,
    s.site_type,
    s.activity_category,
    s.entry_price,
    GROUP_CONCAT(tm.name) AS available_transports,
    s.latitude,
    s.longitude
FROM Site s
LEFT JOIN Site_Transport st ON s.id_site = st.id_site
LEFT JOIN Transport_Mode tm ON st.id_transport = tm.id_transport
GROUP BY s.id_site;

-- Vue : Hôtels avec statistiques de prix
CREATE VIEW v_hotel_summary AS
SELECT 
    h.id_hotel,
    h.name,
    h.star_rating,
    h.beach_name,
    h.price_per_night,
    h.latitude,
    h.longitude,
    CASE 
        WHEN h.price_per_night < 200 THEN 'Budget'
        WHEN h.price_per_night < 300 THEN 'Moyen'
        ELSE 'Luxe'
    END AS price_category
FROM Hotel h;

-- =========================================================
-- ÉTAPE 5 : CRÉATION DES FONCTIONS
-- =========================================================

-- Supprimer fonction si elle existe déjà
DROP FUNCTION IF EXISTS calculate_distance;

-- Fonction : Calcul distance Haversine entre deux points GPS
DELIMITER //
CREATE FUNCTION calculate_distance(
    lat1 DOUBLE, lon1 DOUBLE,
    lat2 DOUBLE, lon2 DOUBLE
) RETURNS DOUBLE
DETERMINISTIC
BEGIN
    DECLARE R DOUBLE DEFAULT 6371; -- Rayon Terre en km
    DECLARE dLat DOUBLE;
    DECLARE dLon DOUBLE;
    DECLARE a DOUBLE;
    DECLARE c DOUBLE;
    
    SET dLat = RADIANS(lat2 - lat1);
    SET dLon = RADIANS(lon2 - lon1);
    SET a = SIN(dLat/2) * SIN(dLat/2) +
            COS(RADIANS(lat1)) * COS(RADIANS(lat2)) *
            SIN(dLon/2) * SIN(dLon/2);
    SET c = 2 * ATAN2(SQRT(a), SQRT(1-a));
    
    RETURN R * c;
END//
DELIMITER ;

-- =========================================================
-- ÉTAPE 6 : CRÉATION DES PROCÉDURES STOCKÉES
-- =========================================================

-- Supprimer procédure si elle existe déjà
DROP PROCEDURE IF EXISTS find_nearest_sites;

-- Procédure : Trouver les N sites les plus proches d'un hôtel
DELIMITER //
CREATE PROCEDURE find_nearest_sites(
    IN p_hotel_id INT,
    IN p_max_results INT
)
BEGIN
    SELECT 
        s.id_site,
        s.name,
        s.site_type,
        s.entry_price,
        calculate_distance(
            h.latitude, h.longitude,
            s.latitude, s.longitude
        ) AS distance_km
    FROM Site s
    CROSS JOIN Hotel h
    WHERE h.id_hotel = p_hotel_id
    ORDER BY distance_km ASC
    LIMIT p_max_results;
END//
DELIMITER ;

-- =========================================================
-- ÉTAPE 7 : CRÉATION DES INDEX ADDITIONNELS
-- =========================================================

CREATE INDEX idx_site_type_price ON Site(site_type, entry_price);
CREATE INDEX idx_hotel_stars_price ON Hotel(star_rating, price_per_night);




-- =========================================================
-- FIN DU SCRIPT
-- Base de données tahiti_travel créée avec succès !
-- =========================================================
