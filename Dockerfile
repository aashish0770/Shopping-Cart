# Stage 1 — Build
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2 — Runtime
FROM eclipse-temurin:17-jdk-jammy

LABEL maintainer="your.email@example.com"
LABEL description="JavaFX Shopping Cart with Localization"
LABEL version="1.0"

# Install native libs + Xvfb + fonts
RUN apt-get update && apt-get install -y --no-install-recommends \
        libgtk-3-0 \
        libglib2.0-0 \
        libxxf86vm1 \
        libxrender1 \
        libxtst6 \
        libxi6 \
        xvfb \
        x11-utils \
        wget \
        unzip \
        fonts-noto \
        fonts-noto-cjk \
    && rm -rf /var/lib/apt/lists/*

# Download JavaFX SDK (needed separately — cannot be shaded into fat jar)
RUN wget -q https://download2.gluonhq.com/openjfx/21.0.2/openjfx-21.0.2_linux-x64_bin-sdk.zip -O /tmp/javafx.zip \
    && unzip -q /tmp/javafx.zip -d /opt/ \
    && rm /tmp/javafx.zip

WORKDIR /app
COPY --from=builder /app/target/javafx-shopping-cart-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["/bin/sh", "-c", "\
    rm -f /tmp/.X99-lock && \
    Xvfb :99 -screen 0 1280x800x24 & \
    sleep 2 && \
    java \
      --module-path /opt/javafx-sdk-21.0.2/lib \
      --add-modules javafx.controls,javafx.fxml,javafx.graphics \
      --add-opens java.base/java.lang=ALL-UNNAMED \
      -jar /app/app.jar \
"]