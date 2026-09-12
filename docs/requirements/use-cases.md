# CoMakers — Cas d'usage du MVP

## Comptes utilisateurs

***UC-01 — Créer un compte***  
UC-02 — Se connecter  
UC-03 — Consulter son profil  
UC-04 — Modifier son profil

## Projets

***UC-05 — Créer un projet***   
UC-06 — Consulter ses projets  
UC-07 — Consulter un projet  
UC-08 — Modifier un projet  
UC-09 — Archiver un projet

## Membres et invitations

***UC-10 — Inviter un collaborateur***  
UC-11 — Consulter ses invitations  
***UC-12 — Accepter une invitation***  
UC-13 — Refuser une invitation  
UC-14 — Consulter les membres d'un projet  
UC-15 — Retirer un membre d'un projet  
UC-16 — Quitter un projet

## Contributions

***UC-17 — Ajouter une contribution***  
UC-18 — Consulter les contributions d'un projet  
UC-19 — Consulter une contribution  
UC-20 — Modifier sa contribution  
UC-21 — Supprimer sa contribution

## Commentaires

UC-22 — Commenter une contribution  
UC-23 — Consulter les commentaires d'une contribution  
UC-24 — Modifier son commentaire  
UC-25 — Supprimer son commentaire

## Tâches

***UC-26 — Créer une tâche***  
UC-27 — Consulter les tâches d'un projet  
UC-28 — Modifier une tâche  
***UC-29 — Assigner une tâche à un membre***  
UC-30 — Changer le statut d'une tâche  
UC-31 — Supprimer une tâche

## Fichiers

***UC-32 — Déposer un fichier***  
UC-33 — Consulter les fichiers d'un projet ou d'une contribution  
UC-34 — Télécharger un fichier  
UC-35 — Supprimer un fichier


```
Utilisateur
↓
crée son compte
↓
crée un projet
↓
invite quelqu'un
↓
l'autre accepte
↓
PROJET COLLABORATIF
│
┌──────┼──────┐
↓      ↓      ↓
Contributions Tâches Fichiers
│
↓
Commentaires
```

## UC-10 — Inviter un collaborateur

### Objectif

Permettre au propriétaire d'un projet d'inviter un utilisateur
CoMakers à rejoindre son projet.

### Acteur principal

Propriétaire du projet (`OWNER`)

### Préconditions

- L'utilisateur est authentifié.
- L'utilisateur est propriétaire du projet.
- Le projet est actif.
- L'utilisateur invité possède un compte CoMakers.
- L'utilisateur invité n'est pas déjà membre du projet.
- Il ne possède pas déjà une invitation active pour ce projet.

### Scénario nominal

1. Le propriétaire consulte son projet.
2. Il demande à inviter un collaborateur.
3. Il sélectionne ou identifie l'utilisateur à inviter.
4. Le système vérifie que cet utilisateur peut être invité.
5. Le système crée une invitation avec le statut `PENDING`.
6. L'invitation devient accessible à l'utilisateur invité.
7. Le système confirme l'envoi de l'invitation.

### Scénarios alternatifs

#### Utilisateur inexistant

L'utilisateur recherché ne possède pas de compte CoMakers.

L'invitation n'est pas créée.

#### Utilisateur déjà membre

L'utilisateur appartient déjà au projet.

L'invitation n'est pas créée.

#### Invitation déjà existante

Une invitation `PENDING` existe déjà pour cet utilisateur et ce projet.

Une seconde invitation n'est pas créée.

#### Utilisateur non autorisé

Un membre qui n'est pas propriétaire tente d'inviter un utilisateur.

L'action est refusée.

### Postconditions

Une invitation `PENDING` existe entre le projet et l'utilisateur invité.

L'utilisateur invité n'est pas encore membre du projet.

### Règles métier associées

- BR-010 — Accès à un projet
- BR-012 — Utilisateur existant
- BR-013 — Gestion des invitations
- BR-016 — Unicité de l'appartenance
- BR-017 — Invitation d'un membre existant