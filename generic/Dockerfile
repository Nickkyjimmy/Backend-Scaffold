# ============
# 🏗️ STAGE 1 - BUILD THE APP
# ============
ARG MY_MODULE=generic

FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

ARG MY_MODULE

# Maven runner
COPY mvnw .
COPY .mvn .mvn

# Dependency
COPY pom.xml .
COPY ${MY_MODULE}_api/pom.xml ${MY_MODULE}_api/pom.xml
COPY ${MY_MODULE}_service/pom.xml ${MY_MODULE}_service/pom.xml

RUN ./mvnw dependency:go-offline -U

# Copy the full source code
COPY ${MY_MODULE}_api/src ${MY_MODULE}_api/src
COPY ${MY_MODULE}_service/src ${MY_MODULE}_service/src

# Build the Spring Boot application
RUN ./mvnw clean package -DskipTests

# ============
# 🚀 STAGE 2 - RUN THE APP
# ============
FROM eclipse-temurin:21-jre-alpine AS runner

ARG MY_MODULE

# Add a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

COPY --from=builder /app/${MY_MODULE}_service/target/*.jar app.jar

# Expose the default Spring Boot port (you can override in compose)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]

