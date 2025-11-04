#!/usr/bin/env bash
set -euo pipefail

APP_NAME="digital_health_backend"
IMAGE_TAG="${APP_NAME}:latest"
CONTAINER_NAME="${APP_NAME}_container"
PORT=8080

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="${ROOT_DIR}/starter-project"

echo "=============================="
echo "Step 1: Build Spring Boot JAR"
echo "=============================="

if [ ! -f "${PROJECT_DIR}/pom.xml" ]; then
  echo "❌ Cannot find pom.xml in ${PROJECT_DIR}"
  exit 1
fi

# Build using system Maven
(cd "${PROJECT_DIR}" && mvn clean package -DskipTests)

echo "=============================="
echo "Step 2: Build Docker image"
echo "=============================="
docker build -t "${IMAGE_TAG}" -f "${ROOT_DIR}/Dockerfile" .

# Stop and remove any existing container
if docker ps -a --format '{{.Names}}' | grep -Eq "^${CONTAINER_NAME}\$"; then
  echo "Stopping existing container ${CONTAINER_NAME}..."
  docker stop "${CONTAINER_NAME}" || true
  docker rm "${CONTAINER_NAME}" || true
fi

echo "=============================="
echo "Step 3: Run container"
echo "=============================="
docker run -d --name "${CONTAINER_NAME}" -p ${PORT}:8080 "${IMAGE_TAG}"

echo "✅ Application started successfully!"
echo "➡  Access it at: http://localhost:${PORT}"
