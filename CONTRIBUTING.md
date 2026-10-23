# Travailler ensemble

## Avant de commencer

Lire README.md, docs/onboarding.md et le périmètre V0.
Choisir une [issue](https://github.com/Pepper-Bots/CoMakers/issues) avec Hélène :
objectif, critères d'acceptation, périmètre et personne responsable.
Si un GitHub Project est utilisé, demander son lien ; les issues restent
accessibles sans dépendre d'un tableau spécifique.

Le dépôt est public. Pour pousser, l'invitation GitHub collaborateur doit être
acceptée ; sinon travailler dans un fork et ouvrir une PR depuis celui-ci.
Aucun secret ou accès à une base commune n'est nécessaire.

## Branche et pull request

Partir d'un main à jour et d'un répertoire sans modifications non sauvegardées :

```text
git switch main
git pull --ff-only origin main
git switch -c feature/nom-court
```

Préfixes : feature/, fix/, docs/, chore/. Une branche regroupe un objectif
cohérent ; convenir de son périmètre avant de modifier les mêmes fichiers.

Avant la PR :
1. Exécuter le Wrapper avec verify depuis backend.
2. Tester les endpoints concernés, dont les refus d'accès.
3. Mettre à jour documentation et exemples si le comportement change.
4. Expliquer les changements de configuration ou de schéma.
5. Vérifier git diff et git status, sans secrets, logs ou dumps de base.

```text
git add <fichiers-concernes>
git commit -m "feat: decrire le resultat"
git push -u origin feature/nom-court
```

Ouvrir une PR vers main ; brouillon tant qu'il reste du travail. Décrire résultat,
tests et limites. Relire les changements et attendre les vérifications CI.
Demander la revue de l'autre personne avant fusion lorsque possible.
Il s'agit d'une convention d'équipe : aucune protection de branche n'est
présumée configurée. Ne pas pousser directement sur main.

Après fusion, revenir sur main et faire git pull --ff-only origin main.
Créer une nouvelle branche pour le prochain objectif. En cas de divergence,
ne pas forcer le push ni réinitialiser le travail d'un collègue ; examiner
l'historique et convenir d'une résolution.

## Conventions de code

- Java 21, packages fonctionnels sous io.github.pepperbots.comakers.
- Contrôleur : contrat HTTP et DTO ; service : règles métier ; repository : accès DB.
- Valider les entrées. Retourner des DTO, jamais une entité contenant passwordHash.
- Tester comportements, erreurs et contrôle d'accès ; pas seulement les getters.
- Ne pas utiliser l'email fourni par le client comme preuve d'identité.
- Pour les futurs projets privés, vérifier l'appartenance en plus du JWT.
- Pas de mot de passe/jeton dans les logs, commits, captures ou valeurs Postman partagées.
- Pas de refonte, nouvelle dépendance ou décision produit sans justification liée à l'issue.

## Base de données et limites

Chaque développeur a sa base. ddl-auto=update est un confort de développement,
pas une stratégie de migration. Avant un changement de schéma affectant les
données d'un collègue, documenter son impact et prévoir les migrations.
Ne pas modifier des données partagées pour faire passer un test.

Les tests actuels simulent la persistance. Le smoke test/CI Docker vérifie le
parcours réel sur MySQL. Signaler explicitement ce qui n'a pas été testé.

## Définition de terminé

Objectif et critères satisfaits, tests adaptés réussis, configuration documentée,
absence de secrets, PR relue et fusionnée. Mettre à jour l'issue après validation.
Une compilation réussie ne signifie pas que le MVP entier est terminé.
