# Etapa de build
FROM maven:3-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .

# Dá permissão de execução para o mvnw
RUN chmod +x mvnw

RUN ./mvnw clean package -DskipTests

# Etapa runtime
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
