# Exercices Hibernate JPA

Exemples pratiques destinés aux étudiants du CÉGEP pour apprendre Jakarta Persistence (JPA) avec Hibernate sur une base H2 en mémoire. Chaque classe ``al420445.ExXX`` couvre un thème précis (mappage d'entités, dirty checking, patrons DAO, JPQL, transactions, journalisation, etc.), de façon à pouvoir ouvrir, exécuter et commenter le fichier isolément pendant le cours.

## Prérequis
- Java 25 (conformément au ``pom.xml``)
- Apache Maven 3.9+
- Aucune base externe : H2 tourne en mode embarqué et ``TcpServer`` démarre un serveur TCP pour inspecter les données avec un client SQL.

## Démarrage rapide
```bash
# Résoudre les dépendances et compiler
mvn clean compile

# Lancer une classe d'exemple via Maven Exec (adapter mainClass au besoin)
mvn -q exec:java -Dexec.mainClass=al420445.Ex01_PersistEntities
```
La plupart des exemples gardent la JVM active à la fin afin que les étudiants puissent se connecter au serveur TCP H2 et observer l'état persistant.

## Aperçu des exercices
| Classe | Sujet principal |
| --- | --- |
| ``Ex01_PersistEntities`` | Mapping @Entity, @OneToMany, @Embedded et héritage; insertion de données de départ. |
| ``Ex02_DirtyChecking`` | Gestion des entités managées vs détachées, dirty checking automatique et ``EntityManager.merge``. |
| ``Ex02B_DirtyChecking`` | Variantes supplémentaires de dirty checking. |
| ``Ex03_DaoPattern`` | Introduction d'une couche DAO autour de l'EntityManager. |
| ``Ex04_MergeAndRemove`` | Utilisation de ``merge``/``remove`` sur des entités détachées. |
| ``Ex05_JpqlQuery`` | JPQL de base avec ``Query`` et ``TypedQuery``. |
| ``Ex06_JoinFetchAndDTO`` | 25 exemples JPQL (joins, projections DTO, pagination, agrégats, stratégies fetch, opérations bulk, etc.). |
| ``Ex07_CascadePersist`` | Règles de cascade et gestion des orphelins. |
| ``Ex08_NavigateRelations`` | Navigation paresseuse vs immédiate dans les relations. |
| ``Ex09_FindByName`` | Patron DAO combiné à des requêtes JPQL paramétrées. |
| ``Ex10_CompareTransactionApproaches`` | Transactions manuelles vs helpers dédiés. |
| ``Ex11_AirportDaoExamples`` | Exécution des méthodes réutilisables de ``dao.examples.AirportDaoExamples``. |

Packages complémentaires :
- ``al420445.airport`` – Modèle d'entités (Airport, Passenger, hiérarchie Ticket, Address, DTOs).
- ``al420445.dao.base`` – Contrats DAO, implémentations et helpers transactionnels (interface fonctionnelle et patron template method).
- ``al420445.dao.examples`` – Démos DAO réutilisables consommées par Ex11.
- ``al420445.dao.tx`` – Infrastructure transactionnelle qui encapsule le boilerplate ``EntityManager``.
- ``al420445.service`` – Couche service montrant comment orchestrer les DAO.

## Journalisation et configuration
- Unité de persistance : ``META-INF/persistence.xml`` (``hibernate2.ex1``) configure Hibernate 6.2 avec H2.
- Journalisation : ``src/main/resources/logback.xml`` route les logs Hibernate (``org.hibernate.SQL``, ``org.hibernate.type``) vers Logback. Ajuster les niveaux dans ce fichier :
  - ``<logger name="org.hibernate.SQL" level="INFO"/>`` réduit le bruit SQL.
  - Remettre ``DEBUG`` pour voir toutes les requêtes.
- ``hibernate.show_sql`` est défini dans ``persistence.xml``; pour le basculer à l'exécution, fournir un ``Map<String, Object> overrides`` lors de ``Persistence.createEntityManagerFactory``.

## Conseils pédagogiques
- Lancer ``Ex01`` en premier pour peupler le schéma, puis réutiliser ces données dans les exercices suivants.
- Inviter les étudiants à ouvrir la console H2 pendant qu'un exemple tourne et se connecter à ``jdbc:h2:tcp://localhost/mem:test;MODE=LEGACY`` (utilisateur ``sa``, mot de passe vide).
- Montrer l'échange entre exécuteurs transactionnels en modifiant ``AirportDaoFactory`` pour choisir entre template method et interface fonctionnelle.
- Pendant une séance sur la journalisation, modifier ``logback.xml`` en direct pour illustrer l'effet des niveaux INFO/DEBUG.

## Dépannage
- Si Maven signale un problème de version Java, vérifier que ``JAVA_HOME`` pointe vers JDK 25 puis relancer ``mvn -version``.
- Avant de relancer un exemple, arrêter la JVM précédente (elle maintient le serveur H2 TCP) ou changer le port dans ``TcpServer`` pour éviter les collisions.

Bonnes expérimentations, adaptez ces scénarios à votre plan de cours !
