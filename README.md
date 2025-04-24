# JEE - Application de Gestion de Conférences (Microservices)

Ce projet vise à construire une application de gestion de conférences et de keynotes en utilisant une architecture microservices basée sur l'écosystème Spring .

## Architecture Cible (Globale)

L'architecture complète envisagée pour ce projet est la suivante :

1.  **Service de Découverte (Discovery Service - ex: Eureka)** :
    *   Rôle : Permet aux différents microservices de s'enregistrer et de se découvrir mutuellement dans le réseau. Essentiel pour la communication dynamique inter-services.

2.  **Service de Configuration (Config Service - ex: Spring Cloud Config)** :
    *   Rôle : Centralise la gestion des fichiers de configuration pour tous les microservices. Permet de modifier la configuration sans redéployer les services. *(Non implémenté dans cette version)*

3.  **Passerelle API (API Gateway - ex: Spring Cloud Gateway)** :
    *   Rôle : Point d'entrée unique pour toutes les requêtes externes (clients UI, applications mobiles...). Gère le routage vers les bons microservices, peut intégrer la sécurité, le load balancing, le cache, et la résilience (Circuit Breaker). *(Non implémenté dans cette version)*

4.  **Microservice Keynote (keynote-service)** :
    *   Rôle : Gère les données des intervenants (keynotes) : création, lecture, mise à jour, suppression (CRUD). Expose une API REST. S'enregistre auprès du Discovery Service.
    *   Base de données : Possède sa propre base de données (ex: H2, PostgreSQL...).

5.  **Microservice Conférence (conference-service)** :
    *   Rôle : Gère les données des conférences et des avis (reviews) associés (CRUD). Expose une API REST. S'enregistre auprès du Discovery Service.
    *   Interaction : Communique avec `keynote-service` (via OpenFeign) pour récupérer les informations sur l'intervenant associé à une conférence.
    *   Base de données : Possède sa propre base de données.

6.  **Résilience & Monitoring (ex: Resilience4J, Actuator)** :
    *   Rôle : Mécanismes pour gérer les pannes partielles (Circuit Breaker) et surveiller la santé des services. *(Circuit Breaker partiellement configuré dans Gateway/Conference, Actuator présent)*

7.  **Documentation API (ex: OpenAPI/Swagger)** :
    *   Rôle : Génère automatiquement la documentation des API REST exposées par les microservices.




## État Actuel de l'Implémentation (Ce Commit)

Dans la version actuelle poussée sur ce dépôt, **seuls les modules fonctionnels suivants ont été développés et configurés** :

1.  **`keynote-service`** :
    *   Fonctionnalités : Entités JPA (Keynote), Repository (DAO), Service métier, DTO, Mapper simple, Contrôleur REST pour le CRUD des keynotes.
    *   Configuration : Base de données H2 en mémoire, enregistrement basique auprès d'Eureka (si Eureka était lancé), documentation Swagger de base.

2.  **`conference-service`** :
    *   Fonctionnalités : Entités JPA (Conference, Review avec relation OneToMany), Repositories, Services métier, DTOs, Mappers simples, Contrôleurs REST pour le CRUD des conférences et potentiellement des reviews (selon avancement).
    *   Interaction : Configuration d'un client OpenFeign pour appeler `keynote-service` (nécessite que `keynote-service` et Eureka soient en cours d'exécution pour fonctionner).
    *   Configuration : Base de données H2 en mémoire, enregistrement Eureka, documentation Swagger de base.

**Les modules techniques (`discovery-service`, `config-service`, `gateway-service`) ainsi que l'intégration complète des fonctionnalités (tests avancés, configuration fine de la résilience, déploiement...) ne font pas partie de ce commit initial et devront être ajoutés ultérieurement pour réaliser l'architecture cible complète.**

