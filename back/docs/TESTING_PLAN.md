# ✅ Planning — Projet 5 : Testing (Front / Back / E2E) — yoga-app

## 🎯 Exigences (Testing Plan)
- [ ] Couverture **≥ 80%** sur tous les indicateurs (instructions / branches / lignes / etc.)
- [ ] **≥ 30% de tests d’intégration**
- [ ] Tests sur **Front-end, Back-end et End-to-End**
- [ ] ⚠️ Back-end : **ne pas tester le package DTO** (et idéalement l’exclure de la couverture)

Source : Testing plan (pages 1–2)

---

## ✅ Matrice des features à couvrir (selon le Testing Plan)

### 1) Login
- [ ] Connexion OK
- [ ] Gestion des erreurs si mauvais login/password
- [ ] Erreur si champ obligatoire manquant

### 2) Register
- [ ] Création de compte OK
- [ ] Erreur si champ obligatoire manquant

### 3) Sessions
- [ ] Affichage de la liste des sessions
- [ ] Boutons **Create** et **Detail** visibles si utilisateur connecté = **admin**

### 4) Informations session
- [ ] Les infos de la session s’affichent correctement
- [ ] Bouton **Delete** visible si utilisateur connecté = **admin**

### 5) Création session
- [ ] La session est créée
- [ ] Erreur si champ obligatoire manquant

### 6) Suppression session
- [ ] La session est correctement supprimée

### 7) Modification session
- [ ] La session est modifiée
- [ ] Erreur si champ obligatoire manquant

### 8) Account
- [ ] Affichage des informations de l’utilisateur

### 9) Logout
- [ ] Déconnexion de l’utilisateur

---

## 🧪 Stratégie pour atteindre 80% + 30% intégration
### Back-end (objectif : gros impact couverture + intégration)
- Unit tests : services (règles métier, validations), utilitaires
- Integration tests (pour atteindre ≥30%) :
    - controllers + sécurité (roles admin/user)
    - repositories (JPA)
    - endpoints create/update/delete session

⚠️ DTO : ne pas tester + exclure de la couverture si possible.

### Front-end
- Unit tests : composants (forms, affichage, conditions admin), services (API), guards
- Integration “légère” : component + service avec HttpTestingController

### E2E (scénarios fonctionnels)
- Parcours utilisateur (register/login/logout)
- Parcours sessions (liste + détails)
- Parcours admin (create/update/delete)

---

## 📅 Planning (10 jours ouvrés — efficace et étape par étape)

### J1 — Setup & baseline
- [ ] Cloner / installer front + back
- [ ] Lancer l’application (front/back/db)
- [ ] Lancer les tests existants (si présents) + noter la couverture initiale
- [ ] Repérer les écrans/features liés au plan (login/register/sessions/account/logout)

✅ Sortie J1 : projet exécutable + état initial couverture

---

### J2 — Mise en place outils de test & couverture
- [ ] Back : config tests + génération couverture (Jacoco)
- [ ] Front : config tests + couverture (Angular coverage)
- [ ] E2E : vérifier outil présent (Cypress/Playwright)
- [ ] Mettre scripts utiles (package.json/gradle/maven selon repo)
- [ ] Préparer exclusion DTO côté back (couverture)

✅ Sortie J2 : commandes tests + coverage OK

---

### J3 — Back : tests unitaires (services)
- [ ] Login/Register : validations + erreurs
- [ ] Sessions : logique métier create/update/delete
- [ ] Cas erreurs (exceptions, champs manquants)

✅ Objectif J3 : monter vite la couverture back sur la logique métier

---

### J4 — Back : tests d’intégration (part 1)
- [ ] Auth/roles (admin vs user) sur endpoints
- [ ] Sessions : list + detail (controllers)
- [ ] Vérifier statuts HTTP + payload attendu

✅ Objectif J4 : contribuer fortement au quota ≥30% intégration

---

### J5 — Back : tests d’intégration (part 2) + stabilisation
- [ ] Création session : OK + champ manquant
- [ ] Modification session : OK + champ manquant
- [ ] Suppression session : OK
- [ ] Générer rapport Jacoco final back + vérifier % (hors DTO)

✅ Milestone J5 : back solide + couverture back proche cible

---

### J6 — Front : tests unitaires (composants)
- [ ] Login : form + erreurs (mauvais identifiants / champ manquant)
- [ ] Register : form + erreurs (champ manquant)
- [ ] Sessions : affichage liste
- [ ] UI admin : boutons Create/Detail visibles admin

✅ Sortie J6 : UI principale couverte

---

### J7 — Front : services + intégration légère
- [ ] Services API (HttpClientTestingModule + HttpTestingController)
- [ ] Informations session : affichage détails + delete visible admin
- [ ] Account : affichage infos user
- [ ] Logout : déconnexion (état/session/token + redirection)

✅ Milestone J7 : couverture front en bonne voie + rapport coverage front

---

### J8 — E2E : parcours utilisateur
- [ ] Register → Login → Logout
- [ ] Liste sessions → Détails session
- [ ] Assertions UI principales (erreurs + redirections)

✅ Sortie J8 : E2E stable (non flaky)

---

### J9 — E2E : parcours admin + consolidation
- [ ] Admin : Create session
- [ ] Admin : Update session
- [ ] Admin : Delete session
- [ ] Vérifier cohérence avec le plan de test (toutes les features cochées)

✅ Sortie J9 : plan de test couvert + preuve E2E

---

### J10 — Livraison
- [ ] Vérifier couverture globale ≥ 80% (front + back)
- [ ] Vérifier ≥ 30% intégration (back surtout)
- [ ] Push GitHub (tests + config + scripts)
- [ ] README :
    - [ ] comment lancer back/front
    - [ ] comment lancer tests (unit/int/e2e)
    - [ ] où trouver les rapports de couverture
- [ ] Préparer présentation : stratégie + démo tests + couverture

✅ Livrables finaux : repo + README + rapports + conformité plan

---
