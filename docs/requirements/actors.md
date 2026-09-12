# CoMakers — Acteurs

## 1. Objet

Ce document décrit les acteurs interagissant avec CoMakers.

Dans le MVP, CoMakers est principalement destiné à des personnes appartenant
déjà à un même cercle de connaissances et souhaitant collaborer autour d'un
projet créatif.

La plateforme doit permettre une collaboration principalement asynchrone afin
que chaque participant puisse contribuer selon ses disponibilités.

---

## 2. Acteurs du MVP

### 2.1 Utilisateur

Un utilisateur est une personne disposant d'un compte CoMakers.

Il peut notamment :

- s'authentifier ;
- créer un projet ;
- consulter les projets auxquels il participe ;
- être invité à rejoindre un projet ;
- accepter ou refuser une invitation ;
- contribuer à un projet dont il est membre ;
- commenter des contributions ;
- créer ou suivre des tâches ;
- déposer et consulter des fichiers.

Un même utilisateur peut participer à plusieurs projets.

---

### 2.2 Propriétaire d'un projet

Le propriétaire (`OWNER`) est l'utilisateur ayant créé le projet.

Dans le MVP, un projet possède un seul propriétaire.

Le propriétaire est également membre du projet.

Il dispose de permissions supplémentaires lui permettant notamment de :

- modifier les informations générales du projet ;
- inviter des utilisateurs ;
- gérer les membres ;
- attribuer ou modifier leurs rôles ;
- archiver le projet.

Le rôle de propriétaire est lié au projet et non au compte utilisateur.

Un utilisateur peut donc être :

- propriétaire d'un projet ;
- simple membre d'un autre projet.

---

### 2.3 Membre d'un projet

Un membre (`MEMBER`) est un utilisateur participant à un projet sans en être
le propriétaire.

Il peut notamment :

- consulter le projet ;
- consulter ses membres ;
- créer des contributions ;
- consulter les contributions ;
- commenter ;
- créer et consulter des tâches ;
- se voir attribuer une tâche ;
- déposer et consulter des fichiers ;
- quitter le projet.

Les actions accessibles à un membre sont toujours limitées au périmètre des
projets auxquels il appartient.

---

## 3. Rôle créatif

Le rôle créatif d'un membre décrit sa fonction dans un projet.

Exemples :

- auteur ;
- illustrateur ;
- développeur ;
- designer ;
- musicien ;
- compositeur ;
- photographe ;
- vidéaste ;
- monteur ;
- traducteur.

Ce rôle est différent du niveau de permission.

Par exemple :

```text
Utilisateur : Hélène

Projet : Livre pour enfants

Permission :
MEMBER

Rôle dans le projet :
Illustratrice