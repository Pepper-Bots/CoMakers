# API disponible

Base URL : http://localhost:8080 en local/IDE, http://localhost:8081 avec Compose.
JSON en entrée ; aucun frontend. Les exemples utilisent uniquement des données
fictives. Envoyer Content-Type: application/json pour les POST.

| Méthode et chemin | Authentification | Succès | Erreurs attendues |
|---|---|---|---|
| POST /api/users/register | Aucune | 201, compte public | 400 validation, 409 email utilisé |
| POST /api/auth/login | Aucune | 200, accessToken/tokenType/expiresIn | 400 données invalides, 401 identifiants incorrects |
| GET /api/auth/me | Bearer JWT | 200, compte public | 401 jeton absent/invalide/expiré ou compte disparu |
| GET /actuator/health | Aucune | 200, status UP | 503 si dépendance indisponible |

## Inscription

```json
{"displayName":"Test CoMakers","email":"test@example.com","password":"TestLocal123!"}
```

Nom non vide, 255 caractères maximum ; email valide, 255 caractères maximum ;
mot de passe de 8 caractères minimum et 72 octets UTF-8 maximum (BCrypt).
Le nom est nettoyé aux extrémités, l'email stocké en minuscules.
L'inscription ne connecte pas automatiquement.

Réponse 201, exemple :
```json
{"id":1,"displayName":"Test CoMakers","email":"test@example.com","createdAt":"2026-10-23T10:00:00"}
```

## Connexion

```json
{"email":"test@example.com","password":"TestLocal123!"}
```

Réponse 200 :
```json
{"accessToken":"<jeton signé>","tokenType":"Bearer","expiresIn":900}
```

Mauvais mot de passe et compte inconnu renvoient la même erreur. Les réponses
d'erreur métier utilisent ProblemDetail (status/detail notamment) ; ne pas
dépendre du texte exact pour le code client.

## Profil

GET /api/auth/me avec l'en-tête :
```text
Authorization: Bearer <accessToken>
```

Même forme de compte public que l'inscription ; jamais de hash/mot de passe.
Dans Postman : sélectionner Bearer Token et coller uniquement le jeton.

Retirer le jeton donne 401. Une copie reste utilisable jusqu'à expiration :
pas de révocation ni endpoint logout dans cette version. Refaire login après
15 minutes. Un jeton signé n'est pas un contenu chiffré.

## Test Postman depuis une base vide

Importer la collection backend/postman/CoMakers-auth.postman_collection.json.
Modifier baseUrl selon le parcours. Exécuter dans l'ordre :

0. Inscription : génère email et mot de passe fictifs, attend 201.
1. Connexion : attend 200 et conserve le jeton localement.
2. Mon profil : attend 200 et compare l'email.
3. Sans jeton : attend 401.
4. Mauvais mot de passe : attend 401.
5. Email déjà utilisé : attend 409.
6. Inscription invalide : attend 400.

La requête 0 crée un nouveau compte à chaque envoi. Pour un compte existant,
renseigner email/password localement et commencer à la requête 1.
Ne pas partager les variables remplies, ni commiter un export contenant un jeton.
Chaque développeur garde sa propre collection/variables locales.
