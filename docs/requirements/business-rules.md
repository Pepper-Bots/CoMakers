
---

### `docs/requirements/business-rules.md`

Pour celui-ci, les identifiants `BR-xxx` vont devenir utiles ensuite : dans tes use cases et même dans tes tests, tu pourras faire référence à `BR-012` au lieu de recopier la règle partout.

# CoMakers — Règles métier

## 1. Objet

Ce document définit les principales règles métier applicables au MVP de
CoMakers.

Ces règles décrivent le comportement fonctionnel attendu indépendamment de
leur implémentation technique.

---

# 2. Utilisateurs

## BR-001 — Unicité du compte

Une adresse e-mail ne peut être associée qu'à un seul compte utilisateur.

Informations obligatoires
→ nom d'affichage
→ e-mail
→ mot de passe

Vérification de l'adresse e-mail
→ NON pour le premier MVP

Connexion automatique après inscription
→ NON
→ redirection vers la connexion

Mot de passe
→ minimum 8 caractères
→ pas de règles artificielles du genre
"obligatoirement une majuscule + chiffre + symbole"

---

## BR-002 — Authentification

Un utilisateur doit être authentifié pour accéder aux fonctionnalités
collaboratives de CoMakers.

---

## BR-003 — Participation à plusieurs projets

Un utilisateur peut participer à plusieurs projets simultanément.

Son rôle et ses permissions sont déterminés indépendamment pour chaque projet.

---

# 3. Projets

## BR-004 — Création d'un projet

Tout utilisateur authentifié peut créer un projet.

---

## BR-005 — Propriété du projet

L'utilisateur qui crée un projet devient automatiquement son propriétaire
(`OWNER`).

---

## BR-006 — Propriétaire obligatoire

Un projet doit toujours posséder un propriétaire.

Dans le MVP, un projet possède exactement un propriétaire.

---

## BR-007 — Appartenance automatique du propriétaire

Le propriétaire d'un projet est automatiquement membre de celui-ci.

Il ne doit pas être nécessaire de créer une invitation pour le propriétaire.

---

## BR-008 — États d'un projet

Dans le MVP, un projet possède l'un des états suivants :

- `ACTIVE`
- `ARCHIVED`

Un projet nouvellement créé possède l'état `ACTIVE`.

---

## BR-009 — Archivage

Seul le propriétaire peut archiver un projet dans le MVP.

Un projet archivé n'est plus considéré comme un projet actif.

Son contenu doit néanmoins rester consultable par ses membres.

---

## BR-010 — Accès à un projet

Dans le MVP, un projet est privé.

Seuls son propriétaire et ses membres peuvent accéder à son contenu.

Un utilisateur authentifié qui ne fait pas partie du projet ne doit pas
pouvoir accéder à ses données.

---

# 4. Membres et invitations

## BR-011 — Invitation obligatoire

Dans le MVP, un utilisateur rejoint un projet existant par invitation.

---

## BR-012 — Utilisateur existant

Dans le MVP, une invitation ne peut cibler qu'un utilisateur disposant déjà
d'un compte CoMakers.

L'invitation par e-mail d'une personne non inscrite est reportée à une
version ultérieure.

---

## BR-013 — Gestion des invitations

Seul le propriétaire du projet peut inviter un utilisateur dans le MVP.

---

## BR-014 — Réponse à une invitation

L'utilisateur invité peut :

- accepter l'invitation ;
- refuser l'invitation.

Une invitation possède initialement l'état `PENDING`.

Elle évolue ensuite vers :

- `ACCEPTED`
- `DECLINED`

---

## BR-015 — Acceptation d'une invitation

L'acceptation d'une invitation crée l'appartenance de l'utilisateur au projet.

---

## BR-016 — Unicité de l'appartenance

Un utilisateur ne peut être membre qu'une seule fois d'un même projet.

Le couple suivant doit être unique :

project + user

## BR-049 — Informations nécessaires à l'inscription

La création d'un compte nécessite :

- un nom d'affichage ;
- une adresse e-mail valide ;
- un mot de passe.

---

## BR-050 — Mot de passe

Un mot de passe doit comporter au minimum 8 caractères.

Le mot de passe ne doit jamais être stocké en clair.

---

## BR-051 — Vérification de l'adresse e-mail

La vérification de l'adresse e-mail n'est pas requise dans le MVP.

---

## BR-052 — Connexion après inscription

La création d'un compte n'authentifie pas automatiquement l'utilisateur.

Après son inscription, l'utilisateur doit se connecter.