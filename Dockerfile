# ------ STAGE 1: BUILD ------
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY gradle.properties .

# Копируем Gradle-скрипты и исходники
COPY gradlew .
COPY gradlew.bat .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src ./src

RUN chmod +x gradlew

# Запускаем сборку Ktor-приложения
RUN ./gradlew installDist --no-daemon

# ------ STAGE 2: RUNTIME ------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Копируем результат сборки из build-образа
COPY --from=build /app/build/install /app/

# Открываем порт для Ktor (если нужно)
EXPOSE 8080

# Запускаем приложение. 
# Предположим, имя папки приложения - "auth" (зависит от вашего group/name проекта)
ENTRYPOINT ["/app/auth-service/bin/auth-service"]
