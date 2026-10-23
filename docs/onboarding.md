# Rejoindre CoMakers et démarrer localement

## 1. Comprendre avant de coder

CoMakers fournit un espace privé de collaboration créative. Le premier usage est
un livre pour enfants. L'inscription et la connexion sont réalisées ; les projets
et la collaboration sont les prochaines étapes. Lire
[le périmètre V0](requirements/mvp-v0.md) puis [l'architecture](architecture/README.md).

L'environnement est indépendant : ne pas demander le mot de passe MySQL ou la clé
JWT d'une autre personne. Aucun compte AWS n'est requis. Workbench et Postman sont
des outils facultatifs ; Workbench seul n'installe pas le serveur MySQL.

Cloner le dépôt dans un répertoire local où l'on peut écrire :

```text
git clone https://github.com/Pepper-Bots/CoMakers.git
cd CoMakers
git status
```

Une fois cette PR fusionnée, partir de main. Avant sa fusion, le kit est sur
`chore/developer-onboarding` : `git switch chore/developer-onboarding`.

## 2. Choisir son parcours

| Parcours | Prérequis | Usage |
|---|---|---|
| A. Tout dans Docker | Git, Docker Linux + Compose v2 | Essayer l'API sans installer Java/MySQL |
| B. Java dans l'IDE + MySQL | Git, JDK 21, MySQL 8.x ou service db Docker | Développer et déboguer |

Le Wrapper télécharge Maven 3.9.11 au premier usage. Ne pas installer Node :
aucun frontend n'existe. Internet/proxy autorisant Maven Central et Docker Hub
est nécessaire pour les premières installations.

## 3. Parcours A — environnement isolé Docker

Vérifier `docker version` : les sections Client ET Server doivent apparaître.
Vérifier `docker compose version`. Sous Windows/macOS, démarrer Docker Desktop
et attendre que le moteur soit prêt. Utiliser des conteneurs Linux.

À la racine, générer .env :

Windows PowerShell :
```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File scripts/init-env.ps1
```

macOS/Linux (OpenSSL nécessaire pour générer les secrets) :
```sh
sh scripts/init-env.sh
```

Les scripts n'écrasent jamais un .env existant. Ils génèrent trois secrets
indépendants sans les afficher. .env est ignoré par Git ; .env.example documente
les champs mais contient volontairement des valeurs vides.

```text
docker compose config --quiet
docker compose up -d --build --wait --wait-timeout 240
docker compose ps
```

Le build exécute les tests Java. Le service db crée la base et son utilisateur
sur un volume neuf. L'API attend que MySQL accepte les connexions.

Attendu : db et api healthy, puis `GET http://localhost:8081/actuator/health`
retourne 200 et `{"status":"UP"}`. L'URL racine n'affiche aucun site :
il s'agit d'une API et non d'une interface graphique.

- API : http://localhost:8081
- MySQL depuis Workbench : 127.0.0.1, port 3307, base comakers_db,
  utilisateur comakers_user, mot de passe DB_PASSWORD de votre .env.
- L'API dans Docker utilise db:3306 ; ne pas y mettre localhost:3307.
- Modifier API_PORT/DB_PORT dans .env si les ports sont déjà occupés.
- Les comptes de votre MySQL installé directement sur la machine ne sont pas
  copiés dans cette nouvelle base.

Après une modification du code : relancer la commande `up` avec `--build`.
Consulter les logs avec `docker compose logs --tail=100 api` ou `db`.

Arrêt en conservant les données : `docker compose down`.
Redémarrage : `docker compose up -d --wait --wait-timeout 240`.
Ne pas ajouter `-v` à down sauf si l'on veut détruire volontairement la base de
test. Changer les mots de passe dans .env ne modifie pas ceux d'un volume MySQL
déjà initialisé : conserver le fichier ou modifier le compte dans MySQL.

## 4. Parcours B — IntelliJ / terminal

Installer un JDK 21 et vérifier `java -version`. Dans IntelliJ :
ouvrir le dépôt, importer backend/pom.xml comme projet Maven, sélectionner
Project SDK 21, Maven Wrapper et JDK 21 pour Maven. Recharger Maven.

### B1. MySQL via Docker seulement

Générer .env comme ci-dessus, puis :

```text
docker compose up -d db --wait --wait-timeout 180
```

Configurer l'application Java avec DB_URL =
`jdbc:mysql://localhost:3307/comakers_db`, DB_USERNAME = comakers_user,
DB_PASSWORD et JWT_SECRET provenant du .env local.
Le .env n'est PAS automatiquement chargé par IntelliJ ou Spring Boot.

### B2. MySQL installé localement, sans Docker

Démarrer MySQL Server 8.x. Depuis Workbench ou le client mysql, avec un compte
administrateur, exécuter en remplaçant le mot de passe de démonstration :

```sql
CREATE DATABASE IF NOT EXISTS comakers_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
CREATE USER IF NOT EXISTS 'comakers_user'@'localhost'
  IDENTIFIED BY 'REMPLACER_PAR_UN_MOT_DE_PASSE_LOCAL';
GRANT ALL PRIVILEGES ON comakers_db.* TO 'comakers_user'@'localhost';
```

Ces privilèges limités à cette base permettent à Hibernate de créer la table
en développement. Ne pas utiliser root dans l'application.
CREATE USER IF NOT EXISTS ne change pas le mot de passe d'un compte existant.
Si la base existe déjà chez vous, conserver votre configuration et vos données.

Générer JWT_SECRET, au choix :

PowerShell :
```powershell
$bytes = New-Object byte[] 32
$rng = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$rng.GetBytes($bytes)
$rng.Dispose()
[Convert]::ToBase64String($bytes)
```

macOS/Linux :
```sh
openssl rand -base64 32
```

Conserver la clé pour les prochains démarrages. Ne pas la commiter ni l'envoyer.

### Variables de l'application

| Variable | Valeur / rôle |
|---|---|
| DB_URL | Défaut : jdbc:mysql://localhost:3306/comakers_db ; 3307 pour db Docker |
| DB_USERNAME | Défaut : comakers_user |
| DB_PASSWORD | Obligatoire : mot de passe de votre utilisateur MySQL |
| JWT_SECRET | Obligatoire : Base64 de 32 octets aléatoires minimum |
| SERVER_PORT | Défaut : 8080 |
| SHOW_SQL | Défaut : false ; true pour afficher le SQL en développement |

Dans IntelliJ : Run > Edit Configurations > CoMakersApplication >
Environment variables. Ajouter les valeurs, puis lancer l'application.
Le champ peut être activé via Modify options. Ne pas versionner la configuration
d'exécution contenant ces secrets.

Ou depuis un terminal, dans backend :

PowerShell :
```powershell
$env:DB_PASSWORD = 'votre-mot-de-passe-local'
$env:JWT_SECRET = 'votre-cle-base64'
# Uniquement si MySQL tourne dans Docker :
# $env:DB_URL = 'jdbc:mysql://localhost:3307/comakers_db'
.\mvnw.cmd spring-boot:run
```

macOS/Linux :
```sh
export DB_PASSWORD='votre-mot-de-passe-local'
export JWT_SECRET='votre-cle-base64'
# export DB_URL='jdbc:mysql://localhost:3307/comakers_db'
./mvnw spring-boot:run
```

API : http://localhost:8080. Contrôle :
GET http://localhost:8080/actuator/health.
Arrêt : bouton Stop d'IntelliJ ou Ctrl+C.

## 5. Vérifier et travailler

Dans backend :
- PowerShell : `.\mvnw.cmd -B -ntp verify`
- macOS/Linux : `./mvnw -B -ntp verify`

Ces tests n'exigent ni MySQL ni secrets. Ils ne prouvent pas à eux seuls la
connexion à la vraie base.

Importer backend/postman/CoMakers-auth.postman_collection.json via Import dans
Postman. Choisir baseUrl = http://localhost:8081 (Docker) ou
http://localhost:8080 (IDE). La collection génère un compte de test et couvre
les succès et les erreurs. Voir [le contrat API](api/authentication.md).

Alternative PowerShell depuis la racine, API déjà démarrée :
```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File scripts/smoke-test.ps1 -BaseUrl http://localhost:8081
```

Avec PowerShell 7 sur macOS/Linux :
```sh
pwsh -File scripts/smoke-test.ps1 -BaseUrl http://localhost:8081
```

Chaque exécution crée un compte fictif distinct ; aucun compte n'est supprimé.
Le script ne touche qu'à l'API indiquée par BaseUrl. Ne pas viser la production.

Pour voir les comptes, sans afficher les hashes :
```sql
SELECT id, display_name, email, created_at FROM comakers_db.users;
```

### Checklist de première installation

- [ ] Je comprends ce qui est livré et ce qui reste prévu.
- [ ] Java/Wrapper ou Docker fonctionne, sans chemin propre à l'ordinateur d'Hélène.
- [ ] Mon API répond UP.
- [ ] Inscription 201, doublon 409, données invalides 400.
- [ ] Connexion 200, profil avec jeton 200, sans jeton 401.
- [ ] Les tests automatisés passent.
- [ ] git status ne montre ni .env ni secrets.
- [ ] J'ai accès au dépôt pour pousser ma première branche.
- [ ] J'ai lu CONTRIBUTING.md et choisi une issue avec l'équipe.

## 6. Dépannage

| Symptôme | Action |
|---|---|
| Could not send request / Invalid protocol | Dans Postman, l'URL commence par http:// ; POST/GET se choisit dans le menu |
| Connection refused | Vérifier port, application démarrée et santé ; 8081 Docker, 8080 IDE |
| JWT_SECRET manquant / Base64 invalide | Définir la variable dans le processus qui lance Java, puis redémarrer |
| Access denied for user | Vérifier DB_USERNAME/DB_PASSWORD et le compte MySQL ; .env ne met pas à jour un volume existant |
| Unknown database | Créer la base ou attendre l'initialisation db Docker |
| Port already allocated | Modifier le port hôte dans .env ; ne pas arrêter un service sans savoir qui l'utilise |
| /me renvoie 401 | Refaire login (15 minutes), choisir Bearer Token et ne coller que le jeton |
| /api/users ou / renvoie 401/403 | Normal : seules les routes documentées sont ouvertes |
| java / JAVA_HOME incorrect | Installer un JDK 21, configurer JAVA_HOME et le JDK Maven d'IntelliJ |
| mvnw non exécutable sur Unix | chmod +x backend/mvnw ; le bit exécutable est versionné |
| Dépendances non téléchargées | Vérifier Internet/proxy Maven Central ; config locale dans ~/.m2/settings.xml |
| Docker Client sans Server | Démarrer/réparer Docker Desktop ; le code CoMakers n'est pas encore lancé |

Sur la machine d'Hélène, Docker Desktop 4.41.2 a signalé un échec de suppression
du socket temporaire dockerInference. Ce problème de démarrage Docker est distinct
de MySQL et du projet. Ne pas faire de reset usine pour tester CoMakers ; utiliser
le parcours B pendant le diagnostic. Les données Docker doivent être préservées.

## 7. Questions produit encore ouvertes

Confirmer avec Hélène : choix du frontend, priorités exactes après le premier lot
projets, contraintes des champs projet, règles détaillées des contributions et
tâches. Ne pas transformer ces points en décisions implicites dans le code.
