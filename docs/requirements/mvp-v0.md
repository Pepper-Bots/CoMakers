# MVP V0 — ordre de réalisation et état

## Vision et premier jalon

Le MVP complet permet une collaboration sur un livre pour enfants.
Le premier jalon V0 est plus court : inscription -> connexion -> création
d'un projet privé -> consultation de ce projet par son créateur.

## État au 23 octobre 2026

| Fonctionnalité | Cas d'usage | État |
|---|---|---|
| Inscription | UC-01 | API et tests réalisés ; validation MySQL/Postman faite |
| Connexion | UC-02 | API JWT et tests réalisés ; validation MySQL/Postman faite |
| Profil courant | UC-03 | GET /api/auth/me réalisé |
| Création d'un projet | UC-05 | À développer |
| Liste / consultation des projets | UC-06 / UC-07 | À développer |
| Modification d'un projet | UC-08 | Prévue après le premier jalon |
| Invitation / acceptation | UC-10 / UC-12 | À développer |
| Membres | UC-14 | À développer |
| Contributions | UC-17 à UC-20 | À développer |
| Commentaires | UC-22 / UC-23 | À développer |
| Tâches | UC-26 à UC-30 | À développer |
| Fichiers | UC-32 à UC-35 | MVP complet ; pas dans le premier jalon V0 |
| Interface utilisateur | Sans endpoint dédié | Absente ; technologie à confirmer |

Les identifiants sont ceux du [catalogue des cas d'usage](use-cases.md).
Le [périmètre MVP complet](mvp.md) reste la cible produit. Ce tableau ne prouve
pas l'achèvement d'une issue GitHub : comparer avec son contenu et ses critères.

## Prochain lot : projets

Créer un projet depuis un compte connecté ; nom obligatoire, description
facultative ; statut ACTIVE ; projet privé ; créateur OWNER et membre automatique.
Consulter seulement les projets autorisés. S'appuyer sur UC-05/UC-07 et les règles
BR-004 à BR-010. Définir les limites de longueur et critères précis dans l'issue
avant de coder.

## Suite envisagée

Collaboration -> contributions -> commentaires -> tâches -> fichiers ->
interface -> test réel du livre pour enfants. L'ordre fin est à confirmer
ensemble selon les retours du premier usage.

## Décisions encore ouvertes

Frontend (Angular envisagé), règles détaillées des contributions/tâches/fichiers,
migrations SQL, déploiement. L'hébergement AWS est une intention future, pas un
prérequis local. La base retenue aujourd'hui est MySQL.
