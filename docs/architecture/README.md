# Architecture actuelle

## Application livrée

Monolithe Spring Boot, API REST, Java 21 et MySQL. Le frontend est absent.

```text
Postman / futur frontend
  -> HTTP + JSON (+ Authorization: Bearer <JWT> pour /me)
  -> SecurityFilterChain
  -> Controller + validation DTO
  -> Service
  -> Repository Spring Data JPA
  -> Hibernate -> MySQL
```

| Package | Responsabilité actuelle |
|---|---|
| user | Entité User, inscription, DTO de réponse sans hash, accès DB |
| auth | Connexion, émission du JWT, profil /me, erreurs de connexion |
| security | BCrypt, signature/validation JWT, routes autorisées |

Lire d'abord UserController/UserService, puis AuthController/AuthService, enfin
SecurityConfig/JwtConfig. Les tests sont sous backend/src/test/java dans les
mêmes packages. AuthApiTest utilise de vrais encodeurs/décodeurs JWT et BCrypt,
avec un repository simulé.

## Persistance

Seule l'entité User est implémentée : table users, identifiant auto-incrémenté,
nom d'affichage, email unique, passwordHash et createdAt.
Hibernate crée/met à jour le schéma via ddl-auto=update.
Aucune migration, base préchargée ou dépendance à un dump personnel.

La contrainte SQL d'unicité protège aussi les inscriptions concurrentes.
La configuration DB_URL/DB_USERNAME/DB_PASSWORD permet de changer d'instance
sans modifier le code. Docker utilise un volume nommé ; Workbench reste un client.

## Authentification et autorisations

POST /api/users/register et POST /api/auth/login sont publics.
GET /actuator/health est public, sans détail sur la base.
GET /api/auth/me exige un JWT valide ; les autres routes sont fermées.

Le JWT HS256 contient le sujet (id utilisateur), émetteur, destinataire, dates
et identifiant du jeton. Il expire après 900 secondes. Pas de cookie de session,
de refresh token ni de révocation immédiate. L'API exige l'en-tête Bearer.
La clé appartient à l'environnement, pas au dépôt.

Un JWT valide prouve l'identité ; il ne donne pas automatiquement accès à tous
les futurs projets. Les règles OWNER/MEMBER restent à implémenter et tester.

## Cible

Le [modèle de domaine](domain-model.md) décrit les prochains concepts, pas le
schéma actuel. Les packages project, membership, contribution, comment, task
et file seront introduits au rythme des fonctionnalités. Pas de microservices
pour ce MVP.

## Vérification

Tests rapides : Wrapper verify, aucune DB.
Test réel : environnement Docker isolé + scripts/smoke-test.ps1 ou Postman.
CI : tests Windows/Linux et smoke test de la stack MySQL/API sur Linux.
