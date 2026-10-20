Créer un compte
↓
Se connecter
↓
Créer un projet
↓
Inviter un collaborateur
↓
Ajouter des contributions
↓
Commenter les contributions
↓
Créer / attribuer des tâches
↓
Suivre l'avancement du projet


CoMakers
│
├── backend
│   └── Spring Boot
│       ├── auth
│       ├── user
│       ├── project
│       ├── membership
│       ├── contribution
│       ├── comment
│       └── task
│
└── frontend
└── à choisir


User
Project
ProjectMember
Invitation
Contribution
Comment
Task

SPRINT 1

Inscription
↓
Connexion
↓
Création d'un projet
↓
Affichage du projet

## Périmètre MVP 

| Priorité | Fonctionnalité | Use case |
|---|---|---|
| 1 | Créer un compte | UC-01 |
| 2 | Se connecter | UC-02 |
| 3 | Créer un projet | UC-03 |
| 4 | Consulter un projet | UC-04 |
| 5 | Modifier un projet | UC-05 |
| 6 | Inviter un collaborateur | UC-07 |
| 7 | Accepter une invitation | UC-08 |
| 8 | Consulter les membres | UC-10 |
| 9 | Ajouter une contribution | UC-11 |
| 10 | Modifier sa contribution | UC-12 |
| 11 | Consulter les contributions | UC-13 |
| 12 | Commenter une contribution | UC-14 |
| 13 | Créer et gérer une tâche | UC-15 + UC associés |


## Backlog de développement

LOT 0 — Socle
Spring Boot démarre
BDD connectée
Architecture packages
Configuration dev

LOT 1 — Utilisateurs
User
Inscription
Connexion
JWT
Sécurisation API

LOT 2 — Projets
Project
Créer
Consulter
Modifier

LOT 3 — Collaboration
ProjectMember
Invitation
Accepter invitation
Lister membres

LOT 4 — Contributions
Contribution
Créer
Modifier
Lister

LOT 5 — Commentaires
Comment
Ajouter
Consulter

LOT 6 — Tâches
Task
Créer
Attribuer
Changer statut
Lister

LOT 7 — Front MVP
Inscription / connexion
Dashboard
Projet
Contributions
Tâches

LOT 8 — Test réel
Création du projet "Livre pour enfants"
Invitation de ta sœur
Utilisation réelle
Bugs
Améliorations


CoMakers/
├── backend/
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           │   └── io/
│           │       └── github/
│           │           └── pepperbots/
│           │               └── comakers/
│           │                   └── CoMakersApplication.java
│           │
│           └── resources/
│               └── application.properties
│
├── docs/
├── .gitignore
└── README.md