# MGP Calculator

MGP Calculator est une application web permettant aux étudiants de calculer leur Moyenne Générale Pondérée (MGP) à partir de leurs notes et unités d’enseignement (UE). L’application propose une interface simple pour saisir les notes, visualiser les résultats et obtenir des informations détaillées sur la performance académique.

## Fonctionnalités

- Saisie interactive des notes et coefficients pour chaque UE
- Calcul automatique de la MGP
- Affichage détaillé des résultats et des statistiques
- Interface utilisateur moderne et responsive
- Architecture séparée backend (Java Spring Boot) et frontend (React)

## Prérequis

### Backend

- Java 17 ou supérieur
- Maven 3.6+ ou Gradle (selon la configuration du projet)
- (Optionnel) Une base de données (ex : H2, MySQL, PostgreSQL) si la persistance est activée

### Frontend

- Node.js (version 20 ou supérieure recommandée)
- npm ou yarn

## Installation et Lancement

### 1. Cloner le dépôt

```bash
git clone https://github.com/ms-teacher1547/mgp-calculator.git
cd mgp-calculator
```

### 2. Lancer le Backend

1. Aller dans le dossier backend (par exemple `src/main/java/com/uy1/mgpcalculator`)
2. Compiler et lancer l’application Spring Boot :

```bash
# Avec Maven
mvn clean install
mvn spring-boot:run

# Ou avec Gradle
./gradlew bootRun
```

3. Le backend sera disponible par défaut sur `http://localhost:8080`

### 3. Lancer le Frontend

1. Aller dans le dossier `frontend` :

```bash
cd frontend
```

2. Installer les dépendances :

```bash
npm install
# ou
yarn install
```

3. Démarrer l’application React :

```bash
npm start
# ou
yarn start
```

4. Le frontend sera accessible sur `http://localhost:5173`

## Configuration

- Le frontend communique avec le backend via des appels API (consultez `frontend/src/services/api.js` pour l’URL de l’API).
- Si besoin, modifiez la configuration de l’URL de l’API pour pointer vers l’adresse de votre backend.

## Structure du projet

```
mgp-calculator/
├── frontend/       # Application React (interface utilisateur)
│   └── src/
│       ├── components/
│       ├── pages/
│       └── services/
└── src/
    └── main/
        └── java/com/uy1/mgpcalculator/   # Application Spring Boot (API backend)
```

## Contribution

Les contributions sont les bienvenues ! N’hésitez pas à ouvrir une issue ou une pull request pour toute amélioration ou correction.

## Licence

Ce projet est sous licence MIT.
