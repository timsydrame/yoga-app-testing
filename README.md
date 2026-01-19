# Yoga App - Full Stack Testing

Application de gestion de sessions de Yoga développée avec Angular 14 et Spring Boot 2.6.1.

**Stack technique** : Angular 14 • Spring Boot 2.6.1 • MySQL 8 • Java 11

**Couverture de code** : Front 80% • E2E 75% • Back 87%

---

## Démarrage rapide

### Prérequis
- Node.js 16+ et npm
- Java 11 et Maven 3.8+
- MySQL 8 (port 3306)

### Installation
```bash
# Cloner le projet
git clone https://github.com/timsydrame/yoga-app-testing.git
cd yoga-app-testing

# Base de données
mysql -u root -p < resources/sql/script.sql

# Configurer application.properties avec vos identifiants MySQL

# Installer les dépendances
cd back && mvn clean install
cd ../front && npm install
```

### Lancer l'application
```bash
# Terminal 1 - Backend (démarrer EN PREMIER)
cd back
mvn spring-boot:run

# Terminal 2 - Frontend
cd front
npm start
```

**URL** : http://localhost:4200

**Compte admin** : yoga@studio.com / test!1234

---

## Tests et couverture

### Lancer tous les tests
```bash
# Tests unitaires front (Jest)
cd front && npm run test:coverage

# Tests E2E (Cypress)
npm run e2e:coverage

# Tests back (JUnit + JaCoCo)
cd ../back && mvn clean test jacoco:report
```

### Rapports de couverture

- **Front** : `front/coverage/jest/lcov-report/index.html`
- **E2E** : `front/coverage/lcov-report/index.html`  
- **Back** : `back/target/site/jacoco/index.html`

---

## Stratégie de tests Back-end

**Couverture atteinte : 87%** (objectif : 80%)

### Packages exclus de la couverture

Les packages suivants sont exclus car ils ne contiennent pas de logique métier :

- **DTOs** : Simples conteneurs de données (getters/setters)
- **Models/Entities** : Entités JPA générées automatiquement
- **Repositories** : Méthodes Spring Data JPA générées par le framework
- **Payloads** : Objets de transfert sans logique

### Focus des tests

Les tests se concentrent sur les composants avec de la vraie logique métier :

- **Services** : Logique métier (UserService, SessionService, TeacherService)
- **Controllers** : Endpoints REST et validation
- **Security** : Authentification JWT et autorisation
- **Mappers** : Transformation entités ↔ DTOs

Cette approche suit les bonnes pratiques de l'industrie en évitant de tester le code généré automatiquement par les frameworks.

---

## Technologies

**Front-end**
- Angular 14 + TypeScript
- Jest (tests unitaires)
- Cypress (tests E2E)

**Back-end**
- Spring Boot 2.6.1 + Java 11
- JUnit 5 + Mockito
- JaCoCo (couverture)
- Spring Security + JWT

**Base de données**
- MySQL 8

---

## Auteur

DRAME Fatoumata - Formation OpenClassrooms Développeur Full Stack

GitHub : [@timsydrame](https://github.com/timsydrame)