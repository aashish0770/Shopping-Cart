# JavaFX Shopping Cart — Localization (SEP2 Week 2)

A JavaFX GUI application that calculates shopping cart totals with full
localization support for **English, Finnish, Swedish, Japanese, and Arabic**.

---

## Project Structure

```
shopping-cart/
├── pom.xml
├── Dockerfile
├── .dockerignore
└── src/
    ├── main/
    │   ├── java/com/shoppingcart/
    │   │   ├── MainApp.java                        ← JavaFX entry point
    │   │   ├── controller/
    │   │   │   └── ShoppingCartController.java     ← FXML controller
    │   │   ├── model/
    │   │   │   ├── CartItem.java                   ← Item model
    │   │   │   └── ShoppingCart.java               ← Cart model
    │   │   └── util/
    │   │       └── LocaleManager.java              ← i18n utility
    │   └── resources/com/shoppingcart/
    │       ├── fxml/
    │       │   ├── ShoppingCart.fxml               ← UI layout
    │       │   └── style.css                       ← Stylesheet
    │       └── i18n/
    │           ├── MessagesBundle.properties       ← Default (English)
    │           ├── MessagesBundle_en_US.properties
    │           ├── MessagesBundle_fi_FI.properties
    │           ├── MessagesBundle_sv_SE.properties
    │           ├── MessagesBundle_ja_JP.properties
    │           └── MessagesBundle_ar_AR.properties
    └── test/
        └── java/com/shoppingcart/model/
            ├── CartItemTest.java
            ├── ShoppingCartTest.java
            └── LocaleManagerTest.java
```

---

## Running Locally

**Prerequisites:** Java 17+, Maven 3.8+, JavaFX 21 (pulled automatically by Maven)

```bash
# Run tests + coverage report
mvn clean test

# View coverage report
open target/site/jacoco/index.html

# Run the GUI
mvn javafx:run
```

---

## Docker

```bash
# Build the image
docker build -t shopping-cart:1.0 .

# Run with a local X11 display (Linux)
docker run -e DISPLAY=$DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix shopping-cart:1.0

# Run headless (Xvfb is started inside the container automatically)
docker run shopping-cart:1.0
```

---

## Localization

| Language | File                              | Locale  |
|----------|-----------------------------------|---------|
| English  | MessagesBundle_en_US.properties   | en_US   |
| Finnish  | MessagesBundle_fi_FI.properties   | fi_FI   |
| Swedish  | MessagesBundle_sv_SE.properties   | sv_SE   |
| Japanese | MessagesBundle_ja_JP.properties   | ja_JP   |
| Arabic   | MessagesBundle_ar_AR.properties   | ar_AR   |

Arabic switches the entire UI to **right-to-left** layout automatically.

---

## Test Coverage (JaCoCo)

Tests cover:
- `CartItem`: price × quantity calculation, validation, setters
- `ShoppingCart`: total summation, addItem, removeItem, clear, edge cases
- `LocaleManager`: locale switching, getString, RTL detection, display names

Minimum enforced coverage: **70% line coverage** (configured in `pom.xml`).

---

## Jenkins Pipeline (CI/CD) — Suggested Stages

```groovy
pipeline {
    agent any
    stages {
        stage('Checkout')  { steps { checkout scm } }
        stage('Build')     { steps { sh 'mvn clean package -DskipTests' } }
        stage('Test')      { steps { sh 'mvn test' } }
        stage('Coverage')  { steps { sh 'mvn jacoco:report' } }
        stage('Docker Build') {
            steps { sh 'docker build -t youruser/shopping-cart:$BUILD_NUMBER .' }
        }
        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId:'dockerhub', ...)]) {
                    sh 'docker push youruser/shopping-cart:$BUILD_NUMBER'
                }
            }
        }
    }
    post {
        always { junit 'target/surefire-reports/*.xml' }
    }
}
```
