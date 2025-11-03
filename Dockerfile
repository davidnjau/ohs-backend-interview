# ==========================
# Stage 1 — Build the JAR
# ==========================
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /workspace

# Copy only the necessary files first for dependency caching
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source and build
COPY src src
RUN ./mvnw -B clean package -DskipTests

# ==========================
# Stage 2 — Runtime
# ==========================
FROM eclipse-temurin:17-jre-alpine

ARG APP_USER=vbroadcast
RUN addgroup -S ${APP_USER} && adduser -S -G ${APP_USER} ${APP_USER}

WORKDIR /app

# Copy Spring Boot layered JAR
COPY --from=builder /workspace/target/*.jar app.jar

# Optimize JVM options for container usage
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError"

RUN mkdir -p /var/log/vbroadcast && chown -R ${APP_USER}:${APP_USER} /var/log/vbroadcast

USER ${APP_USER}

EXPOSE 8088

# Optional healthcheck
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8088/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Duser.timezone=Africa/Nairobi -jar /app/app.jar"]
