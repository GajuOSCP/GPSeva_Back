# ---------- STAGE 1 : Build ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy everything
COPY . .

# Build the Spring Boot app
RUN mvn clean package -DskipTests


# ---------- STAGE 2 : Run ----------
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy only the built jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run the app
CMD ["java", "-jar", "app.jar"]
