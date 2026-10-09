# EJDEN-CLAUDE — EJDEN (Android natif)

Gestion de boutique : ventes, stock, crédits, factures, finances.
Kotlin · Jetpack Compose · Room · architecture MVVM, sans framework d'injection.

```
app/src/main/java/com/ejden/app/
├── core/    logique pure, testée (Money, SaleMath, Dates)
├── data/
│   ├── db/      Room : entités, DAO, base (schéma versionné dans app/schemas)
│   └── repo/    règles métier + transactions (ex. vente + baisse du stock)
└── ui/      écrans Compose + ViewModels (un dossier par module)
```

Règles du projet
- Montants = `Long` (FCFA entiers). Jamais de `Double` pour l'argent.
- Toute opération multi-tables passe par une transaction (`db.withTransaction`).
- Un écran = un `ViewModel` déclaré dans `AppViewModelProvider`.
- La logique de calcul vit dans `core/` et a un test unitaire.

Build : voir `TERMUX.md` (compilation sur GitHub Actions).
