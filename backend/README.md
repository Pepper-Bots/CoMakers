# Backend CoMakers

Java 21, Maven et MySQL sont nécessaires.

La base locale existante est utilisée par défaut : `comakers_db`, utilisateur
`comakers_user`, port 3306. Définir `DB_PASSWORD` dans l'environnement avant
le démarrage. Ne pas enregistrer ce mot de passe dans Git.

La connexion nécessite également `JWT_SECRET` : une clé aléatoire encodée
en Base64, contenant au moins 32 octets avant encodage. Générer la clé une
seule fois dans PowerShell :

```powershell
$jwtBytes = New-Object byte[] 32
$jwtRandom = [System.Security.Cryptography.RandomNumberGenerator]::Create()
$jwtRandom.GetBytes($jwtBytes)
$jwtRandom.Dispose()
[Convert]::ToBase64String($jwtBytes)
```

Copier le résultat dans la variable d'environnement `JWT_SECRET` de la
configuration IntelliJ de `CoMakersApplication`, à côté de `DB_PASSWORD`.
Cette clé signe les jetons : ce n'est pas un mot de passe utilisateur.
Ne pas la commiter ni la partager. Garder la même clé entre les redémarrages ;
la remplacer invalide les jetons existants. Sans clé valide, le démarrage échoue.

Depuis le dossier backend :

```powershell
mvn test
mvn spring-boot:run
```

Les tests isolent la persistance et ne se connectent pas à MySQL.
Ils utilisent leur propre clé JWT et n'exigent aucune variable locale.
Le démarrage applicatif utilise la base configurée. La configuration actuelle
`ddl-auto=update` peut modifier son schéma ; elle est destinée au développement.

## Inscription

`POST /api/users/register` — JSON :

```json
{
  "displayName": "Alice",
  "email": "alice@example.com",
  "password": "un-mot-de-passe"
}
```

- 201 : compte créé ; réponse avec id, displayName, email et createdAt.
- 400 : données invalides ou JSON mal formé.
- 409 : email déjà utilisé.

Le nom est obligatoire (255 caractères maximum). L'email doit être valide
(255 caractères maximum) et est enregistré en minuscules, sans espaces aux
extrémités. Le mot de passe comporte au moins 8 caractères et au maximum
72 octets UTF-8, limite de BCrypt.

Le mot de passe est haché et n'est jamais retourné. L'inscription ne connecte
pas l'utilisateur. Aucun frontend ni envoi d'email n'est inclus.

## Connexion

`POST /api/auth/login` — No Auth, Body raw JSON :

```json
{
  "email": "alice@example.com",
  "password": "un-mot-de-passe"
}
```

Réponse 200 :

```json
{
  "accessToken": "<jeton signé>",
  "tokenType": "Bearer",
  "expiresIn": 900
}
```

Le jeton expire après 15 minutes. Un email inconnu ou un mot de passe incorrect
renvoie le même message avec un statut 401. Des données invalides ou un JSON
mal formé renvoient 400. Aucune session ni cookie d'authentification n'est créé.

## Utilisateur connecté

`GET /api/auth/me` — dans Postman, Authorization > Bearer Token :
coller uniquement la valeur de `accessToken` dans le champ Token.

Réponse 200 : id, displayName, email, createdAt.
Un jeton absent, expiré, mal signé ou destiné à une autre API renvoie 401.
Un compte supprimé ne peut plus consulter son profil.
Les autres routes restent fermées jusqu'à leur implémentation.

## Collection Postman

Importer `postman/CoMakers-auth.postman_collection.json`.
Renseigner localement les variables `email` et `password` avec un compte
de test existant. Ne pas partager les identifiants ni la variable accessToken.

Exécuter dans l'ordre :

1. Connexion : 200, jeton conservé automatiquement dans accessToken.
2. Mon profil : 200, identité du compte connecté.
3. Sans jeton : 401.
4. Mauvais mot de passe : 401.

Pour simuler la déconnexion, retirer le jeton ou choisir No Auth : /me renvoie
401. Cette version ne propose ni endpoint de déconnexion ni révocation
immédiate : une copie du jeton reste valable jusqu'à son expiration.
Il n'y a pas encore de refresh token ; se reconnecter après expiration.

## Sécurité et périmètre

La signature HS256, l'expiration, l'émetteur et le destinataire sont contrôlés
par Spring Security. Le sujet du jeton est l'identifiant du compte.
Aucun mot de passe, hash ou email n'est inclus dans le jeton.
La protection CSRF est désactivée dans ce modèle d'API sans session,
qui exige un en-tête Authorization explicite et n'authentifie pas par cookie.

La validation utilise le support standard
[Spring Security Resource Server](https://docs.spring.io/spring-security/reference/6.5/servlet/oauth2/resource-server/jwt.html).

Ce lot couvre la connexion locale du MVP. Avant une ouverture publique,
prévoir notamment HTTPS et une limitation des tentatives de connexion.
La connexion n'ajoute aucun changement de schéma MySQL.
