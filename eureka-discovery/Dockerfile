# ============
# 🏗️ STAGE 1 - BUILD THE APP
# ============
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy Maven wrapper & pom.xml first (for efficient caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable and download dependencies — leverage Docker layer caching
RUN chmod +x mvnw && ./mvnw dependency:go-offline

# Copy the full source code
COPY src ./src

# Build the Spring Boot application
RUN ./mvnw clean package -DskipTests

# ============
# 🚀 STAGE 2 - RUN THE APP
# ============
FROM eclipse-temurin:21-jre-alpine AS runner

# Install netcat for healthchecks
RUN apk add --no-cache netcat-openbsd

# Add a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/target/eureka-discovery-*.jar app.jar

# Add debug environment variable
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5006"

# Expose Eureka server port (8761) and debug port
EXPOSE 8761
EXPOSE 5006
# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]


