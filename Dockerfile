FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy POM first for layer caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:17-jdk-jammy

LABEL maintainer="ssuzzan81@gmail.com"
LABEL description="JavaFX Shopping Cart with Localization"
LABEL version="1.0"

RUN apt-get update && apt-get install -y --no-install-recommends \
        libgtk-3-0 \
        libglib2.0-0 \
        libxxf86vm1 \
        libxrender1 \
        libxtst6 \
        libxi6 \
        xvfb \
        fonts-noto \
        fonts-noto-cjk \
        fonts-noto-extra \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy the fat jar from the build stage
COPY --from=builder /app/target/javafx-shopping-cart-1.0-SNAPSHOT.jar app.jar

# Expose nothing (desktop GUI app)
# Use Xvfb virtual display for headless execution
ENV DISPLAY=:99

# Entrypoint: start virtual display then launch app
ENTRYPOINT ["/bin/sh", "-c", "Xvfb :99 -screen 0 1280x800x24 & sleep 1 && java --add-opens java.base/java.lang=ALL-UNNAMED -jar /app/app.jar"]
