# Travailler sur Termux, compiler sur GitHub Actions

Termux sert à **écrire et envoyer** le code. Le build Android (SDK, AAPT2) se fait
sur GitHub, gratuitement, à chaque `git push`.

## 1. Installation (une seule fois)

```sh
pkg update && pkg upgrade
pkg install git gh openssh micro        # micro = éditeur simple (ou nano / vim)
termux-setup-storage                    # accès aux fichiers du téléphone

git config --global user.name  "Votre nom"
git config --global user.email "vous@exemple.com"
gh auth login                           # GitHub.com > HTTPS > navigateur
```

## 2. Créer le dépôt et envoyer le projet

```sh
cd ~/EJDEN-kotlin                       # dossier du projet décompressé
git init -b main
git add .
git commit -m "Fondations Kotlin"
gh repo create ejden-android --private --source=. --push
```

## 3. Cycle de travail quotidien

```sh
micro app/src/main/java/com/ejden/app/...   # modifier
git add -A && git commit -m "Décrire le changement"
git push

gh run watch                             # suivre le build en direct
gh run download -n ejden-debug-apk       # récupérer l'APK
termux-open EJDEN-debug.apk              # installer (autoriser « sources inconnues »)
```

Si le build échoue : `gh run view --log-failed` affiche la ligne exacte de l'erreur.
Les tests unitaires tournent avant l'APK : un test rouge bloque la publication.

## 4. Signer la version release (plus tard)

```sh
keytool -genkeypair -v -keystore release.jks -alias ejden \
        -keyalg RSA -keysize 2048 -validity 10000
base64 -w0 release.jks > release.b64

gh secret set KEYSTORE_BASE64   < release.b64
gh secret set KEYSTORE_PASSWORD
gh secret set KEY_ALIAS
gh secret set KEY_PASSWORD
```

Puis `git tag v0.1.0 && git push --tags` produit un APK signé.
**Sauvegardez `release.jks` hors du dépôt** : perdu, il est impossible de mettre l'application à jour.

## 5. Bonnes pratiques

- Un petit commit par changement : plus facile de retrouver l'origine d'un échec de build.
- Ne jamais committer `release.jks`, `local.properties` ni de mots de passe (déjà dans `.gitignore`).
- Le dossier `app/schemas/` (créé au premier build) **doit être committé** : il décrit
  la base de données et sert à écrire les migrations.
- Changer le schéma Room = augmenter `version` dans `AppDatabase` **et** écrire la migration.
