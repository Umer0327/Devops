# Multi-stage Dockerfile
# 1) Build the application with Maven (inside the container)
# 2) Copy the resulting JAR into a smaller runtime image

FROM maven:3.8.8-eclipse-temurin-17 AS builder
WORKDIR /build

# Copy only what we need to build (helps with Docker layer caching)
COPY pom.xml ./
COPY src ./src

# Build the project (skip tests for faster image builds; CI can run tests separately)
RUN mvn -B package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the assembled jar from the builder stage
COPY --from=builder /build/target/*.jar app.jar

# Expose application port
EXPOSE 8080

# Default command
ENTRYPOINT ["java", "-jar", "/app/app.jar"]