# ==========================
# Multi-stage Dockerfile for Spring Boot app
# ==========================

# --- Stage 1: Build the Spring Boot JAR ---
FROM eclipse-temurin:17-jdk-focal AS builder

WORKDIR /workspace

# Install Maven since mvnw is not available
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copy only files needed for dependency caching
COPY starter-project/pom.xml starter-project/pom.xml

# Download dependencies first (improves build caching)
RUN mvn -B -ntp -f starter-project/pom.xml dependency:go-offline

# Copy source code and build the app
COPY starter-project starter-project
RUN mvn -B -ntp -f starter-project/pom.xml clean package -DskipTests

# --- Stage 2: Runtime image ---
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copy the fat JAR from builder stage
COPY --from=builder /workspace/starter-project/target/*.jar app.jar

# Create a non-root user
RUN useradd -m appuser
USER appuser

# Expose Spring Boot port
EXPOSE 8080

# Healthcheck (requires Actuator)
HEALTHCHECK --interval=30s --timeout=3s --start-period=20s --retries=3 \
  CMD [ "sh", "-c", "wget -qO- --timeout=2 http://localhost:8080/actuator/health || exit 1" ]

# Java runtime options (can be overridden)
ENV JAVA_OPTS="-Xms128m -Xmx512m -XX:+UseG1GC -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT [ "sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar" ]
