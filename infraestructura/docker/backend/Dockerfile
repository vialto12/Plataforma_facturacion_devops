# ==========================================
# ETAPA 1: BUILD
# ==========================================
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY backend/pom.xml .

COPY backend/src ./src

RUN mvn clean package -DskipTests


# ==========================================
# ETAPA 2: RUNTIME
# ==========================================
FROM eclipse-temurin:21-jre

WORKDIR /app

RUN useradd --system --create-home --uid 1001 appuser

COPY --from=build /app/target/*.jar app.jar

RUN chown appuser:appuser /app/app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
