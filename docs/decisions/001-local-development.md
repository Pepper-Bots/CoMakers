# Décisions de développement local — 23 octobre 2026

## Choix effectifs

- MySQL remplace le choix initial PostgreSQL, à la demande d'Hélène, pour
  conserver le travail déjà réalisé dans Workbench.
- Java 21 et Spring Boot 3.5.6 sont les versions du backend existant.
- Maven Wrapper fixe Maven 3.9.11 et évite une installation personnelle différente.
- MySQL 8.4 est la référence pour l'environnement Docker de développement.
- Docker est une option reproductible ; Java + MySQL locaux restent supportés.
- Les ports Docker hôtes 3307/8081 évitent de prendre ceux de l'installation
  existante 3306/8080. Ils sont configurables.
- Chaque personne génère ses secrets et crée ses comptes de test. Pas de dump requis.
- Les images Docker utilisent des tags de version ; elles ne sont pas verrouillées
  par digest. Les mises à jour correctives de ces tags peuvent donc changer l'image.
- Le JWT dure 15 minutes, sans refresh ni révocation immédiate ; cela reste une
  première étape d'authentification, pas une politique de production complète.

## Limites à traiter plus tard

ddl-auto=update en développement ; pas encore de migrations versionnées.
Pas de frontend, rôles de projets ou contrôle d'appartenance implémentés.
Avant exposition publique : HTTPS, limitation des tentatives, gestion
opérationnelle des secrets, migrations, sauvegardes et politique de session.

## Points à décider avec l'équipe

Choix du frontend, priorité des lots, contraintes produit non documentées,
stratégie de migration, déploiement. Aucun de ces choix n'est imposé par Docker.
