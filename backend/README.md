# Backend CoMakers

API Spring Boot 3.5.6 / Java 21 / MySQL.

- [Guide complet d'installation](../docs/onboarding.md) : Docker ou Java local,
  base vierge, IntelliJ, variables, dépannage.
- [Contrat des routes](../docs/api/authentication.md) : inscription, connexion,
  profil, santé et statuts HTTP.
- [Architecture](../docs/architecture/README.md) et [contribution](../CONTRIBUTING.md).

## Commandes depuis ce dossier

PowerShell :
```powershell
.\mvnw.cmd -B -ntp verify
.\mvnw.cmd spring-boot:run
```

macOS/Linux :
```sh
./mvnw -B -ntp verify
./mvnw spring-boot:run
```

verify n'exige ni base ni secrets. Le démarrage exige MySQL et les variables
DB_PASSWORD/JWT_SECRET. DB_URL, DB_USERNAME, SERVER_PORT et SHOW_SQL sont
configurables. Le fichier .env à la racine sert à Docker Compose ; Spring Boot
et IntelliJ ne le chargent pas automatiquement.

L'application conserve par défaut localhost:3306/comakers_db, comakers_user,
port HTTP 8080. L'environnement Docker expose l'API sur 8081 et MySQL sur 3307.
Il utilise une base distincte ; aucun compte existant n'y est copié.

Importer [la collection Postman](postman/CoMakers-auth.postman_collection.json)
et régler baseUrl selon le parcours. La requête 0 crée un compte fictif ; les
requêtes suivantes valident connexion, profil et erreurs.

Les JWT expirent après 15 minutes, sans renouvellement ni révocation immédiate.
Retirer le jeton simule une déconnexion côté client, mais une copie reste valide
jusqu'à expiration. Aucun frontend n'est présent.
