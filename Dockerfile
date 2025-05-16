# Etapa 1: Build del JAR
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Ejecutar el JAR
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Cambia 4040 si tu app escucha en otro puerto internamente
EXPOSE 4040

ENTRYPOINT ["java", "-jar", "app.jar"]
