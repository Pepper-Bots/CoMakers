# Backend CoMakers

Java 21, Maven et MySQL sont nécessaires.

La base locale existante est utilisée par défaut : `comakers_db`, utilisateur
`comakers_user`, port 3306. Définir `DB_PASSWORD` dans l'environnement avant
le démarrage. Ne pas enregistrer ce mot de passe dans Git.

Depuis le dossier backend :

```powershell
mvn test
mvn spring-boot:run
```

Les tests d'inscription isolent la persistance et ne se connectent pas à MySQL.
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
pas l'utilisateur. Les autres routes restent fermées jusqu'à l'implémentation
de l'authentification. Aucun frontend ni envoi d'email n'est inclus.
