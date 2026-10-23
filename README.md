# CoMakers

CoMakers aide des personnes qui se connaissent déjà à réaliser un projet créatif
ensemble. Le premier cas concret est la création à distance d'un livre pour
enfants : réunir les contributions, les échanges, les tâches et les fichiers.

**État au 23 octobre 2026 : API d'inscription, connexion JWT et consultation du
profil disponibles.** Il n'y a pas encore de frontend ni de gestion des projets.
Le MVP complet n'est donc pas terminé.

## Je rejoins le projet

1. Lire [le périmètre et l'état réel](docs/requirements/mvp-v0.md).
2. Suivre [le guide de démarrage local](docs/onboarding.md).
3. Tester [les trois routes de l'API](docs/api/authentication.md).
4. Lire [l'architecture actuelle](docs/architecture/README.md).
5. Suivre [les règles de contribution](CONTRIBUTING.md) pour sa première PR.

Aucun compte AWS, accès à la base d'Hélène, secret partagé ni donnée privée n'est
nécessaire pour travailler localement. Chaque personne possède sa propre base et
ses propres secrets. Pour pousser une branche sur ce dépôt, demander à Hélène
l'accès collaborateur GitHub ; le clonage du dépôt public et le travail local
n'exigent pas cet accès.

## Démarrage court avec Docker

Prérequis : Git, Docker avec moteur Linux démarré et Compose v2. Windows :
Docker Desktop avec WSL2 ; macOS : Docker Desktop ; Linux : Docker Engine et
plugin Compose. Internet est nécessaire au premier téléchargement des images.

```text
git clone https://github.com/Pepper-Bots/CoMakers.git
cd CoMakers
```

Générer les secrets locaux (une seule fois) :

- Windows PowerShell : `powershell -NoProfile -ExecutionPolicy Bypass -File scripts/init-env.ps1`
- macOS/Linux avec OpenSSL : `sh scripts/init-env.sh`

Puis, depuis la racine du dépôt :

```text
docker compose up -d --build --wait --wait-timeout 240
```

API : http://localhost:8081 ; état de santé :
http://localhost:8081/actuator/health (réponse attendue : `{"status":"UP"}`).

MySQL Docker est accessible sur `127.0.0.1:3307`. Ces ports évitent les ports
habituels de MySQL local (3306) et d'IntelliJ (8080). Aucun compte applicatif
n'est précréé : commencer par l'inscription.

Pour développer dans IntelliJ ou sans Docker, voir les
[deux parcours détaillés](docs/onboarding.md). La configuration existante sur
3306/8080 reste compatible.

## Stack effective

| Élément | Choix actuel |
|---|---|
| Langage | Java 21 |
| Backend | Spring Boot 3.5.6, Spring Web, JPA, Validation, Security |
| Base | MySQL ; environnement portable basé sur MySQL 8.4 |
| Build | Maven 3.9.11 fourni par le Wrapper |
| Authentification | BCrypt + JWT HS256, durée 15 minutes |
| Frontend | Absent, choix à confirmer (Angular envisagé) |
| Cloud | Aucun requis localement ; AWS reste une piste future |

[Décisions techniques et limites](docs/decisions/001-local-development.md).

## Ce qui fonctionne et ce qui reste à faire

- Réalisé : inscription, validation des données, gestion des doublons, connexion,
  profil authentifié, refus des jetons invalides/expirés.
- Prochaine étape : création et consultation de projets privés, propriétaire
  automatiquement membre.
- Ensuite : invitations, contributions, commentaires, tâches, fichiers et interface.
- Hors MVP : recherche publique de collaborateurs, réseau social, contrats et IA.

Les documents fonctionnels décrivent une **cible**, pas une liste de fonctions
déjà livrées. Le [catalogue UC](docs/requirements/use-cases.md) fixe les identifiants
des cas d'usage. Le [périmètre V0](docs/requirements/mvp-v0.md) fixe l'ordre actuel.
Les [règles métier](docs/requirements/business-rules.md) précisent les comportements.

## Tests et outils

Dans `backend` : `./mvnw verify` (macOS/Linux) ou `.\mvnw.cmd verify`
(PowerShell). Seul Java 21 est nécessaire ; ni MySQL ni JWT_SECRET ne sont requis
pour les tests automatisés actuels, qui simulent les repositories.

La collection [Postman](backend/postman/CoMakers-auth.postman_collection.json)
couvre inscription, connexion, profil, doublon et erreurs.
Le [smoke test](scripts/smoke-test.ps1) vérifie le même parcours sur une API
démarrée et crée un compte de test distinct à chaque exécution.

GitHub Actions exécute les tests sur Windows et Linux et vérifie la stack Docker.
Consulter le résultat de la PR : un workflow ajouté n'est pas une preuve de succès
tant que son exécution n'est pas terminée.

## Organisation du dépôt

```text
backend/                 application Java, tests, Wrapper, Dockerfile, collection Postman
docs/requirements/       besoins et périmètre produit
docs/architecture/       architecture actuelle et modèle métier cible
docs/api/                contrat des routes disponibles
docs/decisions/          choix techniques et points encore ouverts
scripts/                 génération des secrets et vérification API réelle
compose.yaml             MySQL + API isolés pour le développement
CONTRIBUTING.md          branches, PR, tests et coordination
```

L'environnement proposé est destiné au développement. Il utilise
`ddl-auto=update`, n'a pas encore de migrations versionnées, de renouvellement
ou révocation immédiate des JWT, ni de limitation des tentatives de connexion.
Ces sujets et HTTPS sont à traiter avant une ouverture publique.
