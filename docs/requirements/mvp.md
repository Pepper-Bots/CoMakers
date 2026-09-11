# CoMakers — MVP

## 1. Objectif

Le MVP de CoMakers doit permettre à plusieurs personnes qui se
connaissent déjà de créer et gérer ensemble un projet créatif à distance.

L'objectif n'est pas encore de mettre en relation des inconnus,
mais de fournir un premier espace fonctionnel de collaboration.

Le premier cas d'utilisation réel est la création collaborative
d'un livre pour enfants.

---

### Ce document vise à répondre aux question suivantes :

→ Qu'est-ce qu'on construit EXACTEMENT maintenant ?

→ Qu'est-ce qui est inclus ?

→ Qu'est-ce qui est exclu ?

→ Quel scénario doit fonctionner ?

→ Quand pourra-t-on dire "le MVP est terminé" ?

## 2. Scénario de référence

Le MVP doit permettre le scénario suivant :

1. Un utilisateur crée un compte.
2. Un second utilisateur crée également un compte.
3. L'un des utilisateurs crée un projet.
4. Il invite le second utilisateur à rejoindre le projet.
5. Le second utilisateur accepte l'invitation.
6. Les deux utilisateurs peuvent accéder au projet.
7. Ils peuvent ajouter des contributions.
8. Ils peuvent commenter les contributions.
9. Ils peuvent déposer et consulter des fichiers.
10. Ils peuvent créer des tâches.
11. Ils peuvent attribuer les tâches aux membres du projet.
12. Ils peuvent suivre l'avancement des tâches.

Le MVP est considéré comme fonctionnel lorsque ce scénario
peut être réalisé intégralement depuis l'application.

---

## 3. Périmètre fonctionnel

### Comptes utilisateurs

- Création d'un compte
- Authentification
- Profil utilisateur minimal

### Projets

- Création d'un projet
- Consultation d'un projet
- Modification d'un projet
- Archivage d'un projet

### Collaboration

- Invitation d'un utilisateur
- Acceptation ou refus d'une invitation
- Consultation des membres
- Attribution de rôles
- Retrait d'un membre
- Possibilité de quitter un projet

### Contributions

- Création d'une contribution
- Consultation des contributions
- Modification d'une contribution
- Suppression d'une contribution
- Commentaires

### Tâches

- Création d'une tâche
- Attribution à un membre
- Modification d'une tâche
- Statuts :
    - TODO
    - IN_PROGRESS
    - DONE
- Échéance facultative

### Fichiers

- Dépôt d'un fichier
- Consultation/téléchargement
- Association d'un fichier à une contribution
- Suppression d'un fichier

---

## 4. Hors périmètre du MVP

Les fonctionnalités suivantes sont explicitement reportées :

- Recherche publique de collaborateurs
- Profils créatifs avancés
- Portfolio
- Projets publics
- Appels à collaboration
- Candidatures
- Matching
- Votes
- Messagerie temps réel
- Notifications avancées
- Calendrier collaboratif
- Versionnement des contributions
- Contrats
- Gestion juridique avancée des droits
- Intelligence artificielle

Une fonctionnalité présente dans cette section ne doit pas être
développée dans le MVP sauf décision explicite de modification du périmètre.

---

## 5. Contraintes techniques

Backend :
- Java
- Spring Boot
- API REST
- Spring Data JPA
- Spring Security

Base de données :
- PostgreSQL

Architecture :
- Monolithe modulaire

Stockage des fichiers :
- Amazon S3 à terme

Frontend :
- À définir

Déploiement :
- AWS

Le développement doit pouvoir être exécuté localement
indépendamment de l'infrastructure AWS.

---

## 6. Sécurité minimale attendue

Le MVP doit notamment garantir :

- le stockage sécurisé des mots de passe ;
- l'authentification des utilisateurs ;
- le contrôle des autorisations ;
- l'isolation des projets privés ;
- le contrôle de l'appartenance à un projet ;
- la validation des données entrantes ;
- l'absence de secrets dans le repository.

---

## 7. Critères de validation du MVP

Le MVP pourra être considéré comme terminé lorsque :

- le scénario de référence fonctionne intégralement ;
- deux utilisateurs réels peuvent collaborer sur un projet ;
- un utilisateur ne peut pas accéder à un projet auquel il n'appartient pas ;
- les principales règles métier sont couvertes par des tests ;
- l'application peut être utilisée pour le projet réel de livre pour enfants ;
- aucun bug bloquant n'empêche le scénario principal.

---

## 8. Validation terrain

Le premier test réel du MVP sera effectué sur le projet de livre
pour enfants.

Les difficultés rencontrées et les besoins observés pendant cette
utilisation serviront à définir les évolutions post-MVP.