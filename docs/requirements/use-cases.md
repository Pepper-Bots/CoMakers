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

---
---

# USE-CASE Détails

## UC-01 — Créer un compte

### Objectif

Permettre à une personne de créer un compte CoMakers afin d'accéder
aux fonctionnalités collaboratives de la plateforme.

### Acteur principal

Visiteur non authentifié.

### Préconditions

- L'utilisateur ne possède pas déjà un compte associé à l'adresse e-mail saisie.

### Scénario nominal

1. Le visiteur demande à créer un compte.
2. Le système affiche le formulaire d'inscription.
3. Le visiteur renseigne :
    - son nom d'affichage ;
    - son adresse e-mail ;
    - son mot de passe.
4. Le visiteur valide son inscription.
5. Le système vérifie la validité des données.
6. Le système vérifie que l'adresse e-mail n'est pas déjà utilisée.
7. Le système sécurise le mot de passe avant son stockage.
8. Le système crée le compte utilisateur.
9. Le système confirme la création du compte.

### Scénarios alternatifs

#### Adresse e-mail déjà utilisée

L'adresse e-mail correspond déjà à un compte CoMakers.

Le compte n'est pas créé et le système informe l'utilisateur que
cette adresse ne peut pas être utilisée pour une nouvelle inscription.

#### Adresse e-mail invalide

L'adresse e-mail ne respecte pas le format attendu.

Le compte n'est pas créé et l'utilisateur est invité à corriger
son adresse.

#### Mot de passe invalide

Le mot de passe ne respecte pas les exigences définies par CoMakers.

Le compte n'est pas créé et l'utilisateur est informé des exigences
à respecter.

#### Données obligatoires manquantes

Une ou plusieurs informations obligatoires ne sont pas renseignées.

Le compte n'est pas créé et les champs concernés sont signalés.

### Postconditions

- Un nouvel utilisateur est enregistré dans CoMakers.
- L'adresse e-mail est associée à un seul compte.
- Le mot de passe n'est jamais stocké en clair.
- L'utilisateur peut utiliser son compte pour s'authentifier.

### Règles métier associées

- BR-001 — Unicité du compte

## UC-05 — Créer un projet

### Objectif

Permettre à un utilisateur authentifié de créer un nouvel espace de
collaboration pour un projet créatif.

### Acteur principal

Utilisateur authentifié.

### Préconditions

- L'utilisateur possède un compte CoMakers.
- L'utilisateur est authentifié.

### Scénario nominal

1. L'utilisateur demande à créer un nouveau projet.
2. Le système affiche le formulaire de création.
3. L'utilisateur renseigne :
    - le nom du projet ;
    - éventuellement une description.
4. L'utilisateur valide la création.
5. Le système vérifie la validité des données.
6. Le système crée le projet avec le statut `ACTIVE`.
7. Le système définit l'utilisateur comme propriétaire (`OWNER`).
8. Le système ajoute automatiquement le propriétaire comme membre du projet.
9. Le projet est créé comme projet privé.
10. Le système confirme la création du projet.
11. L'utilisateur peut accéder au nouvel espace projet.

### Scénarios alternatifs

#### Nom du projet absent

Le nom du projet n'est pas renseigné.

Le projet n'est pas créé et le système indique que le nom est obligatoire.

#### Nom du projet invalide

Le nom ne respecte pas les contraintes définies par CoMakers.

Le projet n'est pas créé et l'utilisateur est invité à corriger le nom.

#### Utilisateur non authentifié

Un utilisateur non authentifié tente de créer un projet.

La création est refusée.

### Postconditions

- Un nouveau projet `ACTIVE` existe.
- Le projet est privé.
- L'utilisateur ayant créé le projet en est le propriétaire.
- Une appartenance au projet existe pour le propriétaire.
- Le propriétaire possède la permission `OWNER`.
- Le projet peut désormais recevoir des membres, contributions, tâches et fichiers.

### Règles métier associées

- BR-002 — Authentification
- BR-004 — Création d'un projet
- BR-005 — Propriété du projet
- BR-006 — Propriétaire obligatoire
- BR-007 — Appartenance automatique du propriétaire
- BR-008 — États d'un projet
- BR-010 — Accès à un projet
