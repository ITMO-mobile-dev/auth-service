# ------------------ СТАДИЯ 1: Сборка ------------------
FROM eclipse-temurin:22-jdk AS build

WORKDIR /app

COPY gradle.properties .
COPY gradlew .
COPY gradlew.bat .
COPY gradle/ ./gradle/

COPY build.gradle.kts .
COPY settings.gradle.kts .

RUN chmod +x gradlew

RUN ./gradlew --no-daemon dependencies

COPY src/ ./src/

RUN ./gradlew installDist --no-daemon

# ------------------ СТАДИЯ 2: Рантайм ------------------
FROM eclipse-temurin:22-jre

WORKDIR /app

COPY --from=build /app/build/install/auth-service/ /app/

EXPOSE 8080

ENTRYPOINT ["/app/bin/auth-service"]