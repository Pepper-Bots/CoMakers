# CoMakers

> Plateforme collaborative permettant de créer, organiser et développer des projets créatifs à plusieurs.

## À propos

Ce projet a pour objectif de faciliter la collaboration entre des personnes souhaitant développer ensemble des projets créatifs.

L'application doit permettre à terme de passer d'une idée à un projet concret en réunissant :

* des porteurs de projets ;
* des créateurs ;
* des collaborateurs disposant de compétences complémentaires ;
* des collectifs déjà constitués.

La plateforme combinera progressivement **gestion de projet**, **collaboration** et **mise en relation**.

---

## Premier cas d'usage

Le développement est guidé par un premier projet réel : la création collaborative à distance d'un **livre pour enfants**.

Ce cas d'usage permettra de tester les fonctionnalités avec de vrais utilisateurs dès les premières versions et d'orienter le développement selon les besoins réellement rencontrés.

---

## Objectif du MVP

La première version doit permettre à plusieurs utilisateurs de gérer ensemble un projet créatif.

### Fonctionnalités principales

* Création et authentification d'un compte
* Création d'un projet
* Modification d'un projet
* Invitation de collaborateurs
* Gestion des membres d'un projet
* Attribution de rôles
* Création de contributions
* Commentaires sur les contributions
* Création et attribution de tâches
* Suivi de l'avancement des tâches
* Dépôt et consultation de fichiers

Les fonctionnalités de réseau social, de recherche publique de collaborateurs et de gestion juridique avancée seront développées ultérieurement.

---

## Vision à long terme

La plateforme devra permettre trois grands usages :

### Créer

Donner une structure à une idée et disposer d'un espace permettant de la transformer en projet.

### Collaborer

Centraliser les contributions, fichiers, tâches, décisions et échanges nécessaires à la réalisation du projet.

### Rencontrer

Permettre aux porteurs de projets de trouver des collaborateurs et aux créateurs de découvrir des projets correspondant à leurs compétences et leurs envies.

---

## Stack technique

### Backend

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Security
* Maven
* API REST

### Base de données

* PostgreSQL

### Frontend

À définir.

Option actuellement envisagée :

* Angular

### Infrastructure

* Docker
* AWS

Services AWS envisagés à terme :

* Amazon RDS — PostgreSQL
* Amazon S3 — stockage des fichiers
* CloudFront / S3 — frontend
* Hébergement du backend AWS à définir

L'infrastructure cloud définitive sera mise en place après validation du prototype local.

---

## Architecture

Le MVP reposera sur une architecture **monolithique modulaire**.

Organisation fonctionnelle envisagée :

```text
backend/
├── authentication/
├── users/
├── projects/
├── members/
├── contributions/
├── tasks/
├── files/
└── common/
```

L'objectif est de conserver une architecture simple tout en isolant correctement les différents domaines métier.

Une architecture microservices n'est pas envisagée pour le MVP.

---

## Structure du repository

Structure envisagée :

```text
/
├── backend/
├── frontend/
├── docs/
├── .gitignore
└── README.md
```

Le frontend sera ajouté une fois la technologie définitivement choisie.

---

## Organisation du développement

Le projet est développé de manière itérative à partir d'un backlog.

Le suivi sera réalisé avec :

* GitHub Issues
* GitHub Projects
* Pull Requests
* sprints courts

### Workflow

```text
Backlog
   ↓
Ready
   ↓
In progress
   ↓
Review / Test
   ↓
Done
```

Chaque sprint doit répondre à un objectif fonctionnel identifiable.

---

## Git workflow

Branche principale :

```text
main
```

Branches de développement :

```text
feature/<nom-feature>
fix/<nom-fix>
chore/<nom-tache>
```

Exemples :

```text
feature/project-creation
feature/user-authentication
feature/project-members
fix/project-access-control
chore/docker-postgres
```

Les évolutions significatives sont intégrées à `main` via Pull Request.

---

## Roadmap

### Phase 0 — Initialisation

* [ ] Initialiser le repository
* [ ] Ajouter le README
* [ ] Ajouter le `.gitignore`
* [ ] Initialiser Spring Boot
* [ ] Configurer PostgreSQL
* [ ] Configurer Docker
* [ ] Créer le GitHub Project
* [ ] Constituer le backlog initial

### Phase 1 — Utilisateurs

* [ ] Modèle utilisateur
* [ ] Création de compte
* [ ] Authentification
* [ ] Autorisation
* [ ] Profil minimal

### Phase 2 — Projets

* [ ] Création d'un projet
* [ ] Consultation d'un projet
* [ ] Modification d'un projet
* [ ] Suppression d'un projet
* [ ] Gestion du propriétaire

### Phase 3 — Collaboration

* [ ] Invitations
* [ ] Membres
* [ ] Rôles
* [ ] Contrôle d'accès

### Phase 4 — Contributions

* [ ] Création d'une contribution
* [ ] Consultation
* [ ] Modification
* [ ] Commentaires

### Phase 5 — Organisation

* [ ] Création de tâches
* [ ] Attribution
* [ ] Statuts
* [ ] Priorités
* [ ] Échéances

### Phase 6 — Fichiers

* [ ] Upload
* [ ] Téléchargement
* [ ] Association à un projet
* [ ] Association à une contribution

### Phase 7 — MVP

Utilisation de l'application sur un premier projet créatif réel et collecte des retours utilisateurs.

---

## Fonctionnalités envisagées après le MVP

* Profils créatifs
* Portfolio
* Recherche de collaborateurs
* Projets publics
* Appels à collaboration
* Système de candidatures
* Votes
* Notifications
* Calendrier collaboratif
* Gestion des disponibilités
* Versionnement des contributions
* Messagerie
* Gestion des droits et accords entre collaborateurs
* Modèles de contrats
* Fonctionnalités d'assistance basées sur l'IA

---

## Sécurité

La sécurité doit être intégrée dès la conception.

Principes initiaux :

* mots de passe hashés ;
* authentification sécurisée ;
* gestion stricte des autorisations ;
* contrôle d'accès aux projets privés ;
* validation des données entrantes ;
* secrets exclus du repository ;
* séparation des environnements ;
* principe du moindre privilège pour les ressources AWS.

Aucune clé, aucun mot de passe ou secret ne doit être commité dans Git.

---

## Documentation

La documentation fonctionnelle et technique sera progressivement centralisée dans :

```text
/docs
```

Elle pourra notamment contenir :

```text
docs/
├── requirements/
├── architecture/
├── api/
└── decisions/
```

---

## Statut

Projet en phase initiale de conception et de développement.

Le périmètre fonctionnel est susceptible d'évoluer selon les retours issus des premiers cas d'utilisation.
