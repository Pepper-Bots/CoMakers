# Modèle de domaine cible

Ce modèle est conceptuel. À ce stade, seule User est une entité Java persistée.
Les autres concepts ne correspondent pas encore à des tables existantes.

```text
User -- ProjectMember -- Project
User -- Invitation ----- Project
                          |-- Contribution -- Comment
                          |-- Task
                          |-- FileAsset
```

- User : compte identifié par email, authentification et profil minimal.
- Project : espace créatif privé, ACTIVE ou ARCHIVED.
- ProjectMember : lien unique utilisateur/projet ; permission OWNER ou MEMBER.
- Invitation : proposition de rejoindre un projet, adressée à un compte existant.
- Contribution : contenu créé par un membre dans un projet.
- Comment : échange lié à une contribution.
- Task : travail à réaliser, statut et attribution.
- FileAsset : fichier associé au projet ou à une contribution.

Règles déjà cadrées : un projet possède exactement un propriétaire, qui en est
aussi membre ; un utilisateur peut appartenir à plusieurs projets avec des
permissions différentes. Voir les règles BR-003 à BR-016.

À détailler avant implémentation : attributs, cardinalités complètes,
contraintes des contenus, permissions de modification/suppression et cycle de
vie des fichiers. Ne pas déduire de ce schéma un droit d'accès non spécifié.
