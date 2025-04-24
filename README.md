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
  
flowchart TD
    classDef clientStyle fill:#E0F2F1,stroke:#004D40,stroke-width:2px,color:#004D40,font-weight:bold
    classDef gatewayStyle fill:#BBDEFB,stroke:#0D47A1,stroke-width:2px,color:#0D47A1,font-weight:bold
    classDef infraStyle fill:#E1BEE7,stroke:#4A148C,stroke-width:2px,color:#4A148C,font-weight:bold
    classDef serviceStyle fill:#C8E6C9,stroke:#1B5E20,stroke-width:2px,color:#1B5E20,font-weight:bold
    classDef dbStyle fill:#FFECB3,stroke:#FF6F00,stroke-width:2px,color:#FF6F00,font-weight:bold
    
    Client[fa:fa-laptop-code Client<br>Web/Mobile/API] --> GW
    
    subgraph Infrastructure["Infrastructure Services"]
        GW[fa:fa-random API Gateway<br>Spring Cloud Gateway]
        DS[fa:fa-compass Discovery Service<br>Eureka/Consul]
        CFG[fa:fa-cogs Config Service<br>Spring Cloud Config]
    end
    
    subgraph Business["Business Services"]
        KS[fa:fa-chalkboard-teacher Keynote Service<br>API: /keynotes]
        CS[fa:fa-users Conference Service<br>API: /conferences, /reviews]
    end
    
    subgraph DataLayer["Data Layer"]
        DB_KS[(fa:fa-database Keynote DB)]
        DB_CS[(fa:fa-database Conference DB)]
    end
    
    GW --> |Route /keynotes/**| KS
    GW --> |Route /conferences/**<br>/reviews/**| CS
    GW --> |Service Discovery| DS
    
    KS --> |Register & Heartbeat| DS
    CS --> |Register & Heartbeat| DS
    
    KS --> |Read/Write| DB_KS
    CS --> |Read/Write| DB_CS
    
    CS --> |API Call via Feign| KS
    
    KS -..-> |Config Fetch| CFG
    CS -..-> |Config Fetch| CFG
    GW -..-> |Config Fetch| CFG
    
    Client:::clientStyle
    GW:::gatewayStyle
    DS:::infraStyle
    CFG:::infraStyle
    KS:::serviceStyle
    CS:::serviceStyle
    DB_KS:::dbStyle
    DB_CS:::dbStyle




## État Actuel de l'Implémentation (Ce Commit)

Dans la version actuelle poussée sur ce dépôt, **seuls les modules fonctionnels suivants ont été développés et configurés** :

1.  **`keynote-service`** :
    *   Fonctionnalités : Entités JPA (Keynote), Repository (DAO), Service métier, DTO, Mapper simple, Contrôleur REST pour le CRUD des keynotes.
    *   Configuration : Base de données H2 en mémoire, enregistrement basique auprès d'Eureka (si Eureka était lancé), documentation Swagger de base.

2.  **`conference-service`** :
    *   Fonctionnalités : Entités JPA (Conference, Review avec relation OneToMany), Repositories, Services métier, DTOs, Mappers simples, Contrôleurs REST pour le CRUD des conférences et potentiellement des reviews (selon avancement).
    *   Interaction : Configuration d'un client OpenFeign pour appeler `keynote-service` (nécessite que `keynote-service` et Eureka soient en cours d'exécution pour fonctionner).
    *   Configuration : Base de données H2 en mémoire, enregistrement Eureka, documentation Swagger de base.

**Les modules techniques (`discovery-service`, `config-service`, `gateway-service`) ainsi que l'intégration complète des fonctionnalités (tests avancés, configuration fine de la résilience ...) ne font pas partie de ce commit initial et devront être ajoutés ultérieurement pour réaliser l'architecture cible complète.**

