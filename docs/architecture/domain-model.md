
---

### `docs/architecture/domain-model.md`

Ici, attention à une distinction : **ce n'est pas encore ton schéma PostgreSQL ni tes classes JPA**. C'est ton **modèle métier conceptuel**. On traduira ensuite ce modèle en architecture Java.

# CoMakers — Modèle de domaine

## 1. Objet

Ce document décrit le modèle de domaine initial du MVP de CoMakers.

Il représente les principaux concepts métier et leurs relations sans imposer
leur implémentation technique ou leur représentation exacte en base de données.

---

# 2. Vue générale

Le domaine du MVP s'articule autour du concept central de `Project`.

```text
                         User
                          │
                          │
                 ┌────────┴────────┐
                 │                 │
                 ▼                 ▼
            Invitation       ProjectMember
                                   │
                                   │
                                   ▼
User ─────────────────────────── Project
                                   │
                    ┌──────────────┼──────────────┐
                    │              │              │
                    ▼              ▼              ▼
              Contribution        Task        FileAsset
                    │
                    │
                    ▼
                 Comment