```markdown
# OHS Digital Health Backend Exercise

A Spring Boot RESTful API application for managing patient healthcare data, including patient records, medical encounters, and clinical observations.

## Table of Contents
- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [API Documentation](#api-documentation)
- [Database Configuration](#database-configuration)
- [Development Guidelines](#development-guidelines)
- [Testing](#testing)
- [Docker Support](#docker-support)
- [Contributing](#contributing)

## Overview

This project implements a healthcare management system with the following core features:
- **Patient Management**: Create, read, update, and delete patient records
- **Encounter Tracking**: Manage patient medical encounters
- **Clinical Observations**: Record and retrieve patient observations
- **Advanced Search**: Filter and search patients by multiple criteria
- **Pagination Support**: Efficient data retrieval with pagination

### Key Features
- RESTful API design with proper HTTP status codes
- Layered architecture (Controller → Service → Repository)
- DTO pattern for API boundaries
- Comprehensive exception handling
- Bean validation for request data
- Swagger/OpenAPI documentation
- Docker containerization support

## Prerequisites

- **Java**: 17 or higher
- **Maven**: 3.8 or higher
- **Docker** (optional): For containerized deployment
- **PostgreSQL**

## Tech Stack

### Core Framework
- **Spring Boot 3.5.6**: Main application framework
- **Spring Web**: RESTful API development
- **Spring Data JPA**: Database persistence layer
- **Spring Validation**: Request validation

### Database
- **PostgreSQL**: Production database support
- **Flyway**: Database migration management
- **Redis**: Caching and data store integration

### Development Tools
- **Lombok**: Reduce boilerplate code
- **Springdoc OpenAPI**: API documentation (Swagger UI)
- **Maven**: Build and dependency management

## Getting Started

### Quick Start

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ohs-backend-developer-exercise
   ```

2. **Navigate to the starter project**
   ```bash
   cd starter-project
   ```

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the API**
    - Base URL: `http://localhost:8080`
    - Swagger UI: `http://localhost:8080/swagger-ui.html`

### Using Docker

1. **Build the Docker image**
   ```bash
   docker build -t ohs-backend .
   ```

2. **Run with Docker Compose**
   ```bash
   docker-compose -f docker-compose-services.yaml up
   ```

## Project Structure

```
starter-project/
├── src/
│   ├── main/
│   │   ├── java/org/example/
│   │   │   ├── controllers/          # REST API endpoints
│   │   │   │   ├── PatientController.java
│   │   │   │   ├── EncounterController.java
│   │   │   │   └── ObservationController.java
│   │   │   ├── services/             # Business logic interfaces
│   │   │   ├── service_implementation/ # Service implementations
│   │   │   ├── repository/           # Data access layer
│   │   │   ├── entity/               # JPA entities
│   │   │   ├── dto/                  # Data Transfer Objects
│   │   │   ├── mappers/              # Entity-DTO mappers
│   │   │   ├── exception/            # Custom exceptions
│   │   │   ├── swagger/              # API documentation config
│   │   │   └── ExerciseApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/         # Flyway migrations
│   └── test/                         # Test classes
├── pom.xml                           # Maven configuration
└── Dockerfile                        # Docker configuration
```

## API Documentation

### Swagger UI
Access interactive API documentation at: `http://localhost:8080/swagger-ui.html`

### Core Endpoints

#### Patient Management
- `POST /api/patients` - Create a new patient
- `GET /api/patients` - List all patients (with pagination and filtering)
- `GET /api/patients/{id}` - Get patient by ID
- `PUT /api/patients/{id}` - Update patient
- `DELETE /api/patients/{id}` - Delete patient
- `GET /api/patients/{id}/encounters` - Get patient encounters
- `GET /api/patients/{id}/observations` - Get patient observations

#### Encounter Management
- `POST /api/encounters` - Create a new encounter
- `GET /api/encounters` - List all encounters
- `GET /api/encounters/{id}` - Get encounter by ID
- `PUT /api/encounters/{id}` - Update encounter
- `DELETE /api/encounters/{id}` - Delete encounter

#### Observation Management
- `POST /api/observations` - Create a new observation
- `GET /api/observations` - List all observations
- `GET /api/observations/{id}` - Get observation by ID
- `PUT /api/observations/{id}` - Update observation
- `DELETE /api/observations/{id}` - Delete observation

### Example Requests

#### Create Patient
```bash
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "dateOfBirth": "1990-01-15",
    "gender": "MALE"
  }'
```

#### Search Patients
```bash
curl "http://localhost:8080/api/patients?firstName=John&page=0&size=10"
```

## Database Configuration

### PostgreSQL (Production)
Update `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ohs_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

## Development Guidelines

### Architecture Principles
1. **Layered Architecture**: Maintain clear separation between layers
    - Controllers: Handle HTTP requests/responses
    - Services: Implement business logic
    - Repositories: Manage data persistence

2. **DTO Pattern**: Use DTOs at API boundaries
    - Request DTOs for incoming data
    - Response DTOs for outgoing data
    - Never expose entities directly

3. **Exception Handling**: Implement proper error handling
    - Use custom exceptions for business logic errors
    - Return appropriate HTTP status codes
    - Provide meaningful error messages

4. **Validation**: Add Bean Validation annotations
    - Validate request data at controller level
    - Use `@Valid` annotation on request bodies
    - Define custom validators when needed

### Code Style
- Follow Java naming conventions
- Use Lombok to reduce boilerplate
- Write meaningful comments for complex logic
- Keep methods focused and concise

## Testing

### Running Tests
```bash
mvn test
```

### Test Structure
- **Unit Tests**: Test individual service methods
- **Integration Tests**: Test controller endpoints
- **Repository Tests**: Test data access layer

## Docker Support

### Dockerfile
The project includes a Dockerfile for containerization:
```bash
chmod +x build_and_run.sh
./build_and_run.sh
```

### Docker Compose
Use `docker-compose-services.yaml` for multi-container setup: 
To get the DB and Redis
```bash
docker-compose -f docker-compose-services.yaml up -d
```

## Configuration

### Application Properties
Key configuration options in `application.properties`:

```properties
# Server Configuration
server.port=8080

# Logging
logging.level.org.example=DEBUG

# API Documentation
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

### Environment Variables
Override properties using environment variables:
This project uses environment variables to configure application settings, database connection, and Redis. Override the properties by setting the variables below.

### Application
| Variable      | Description      | Default/Example |
|---------------|------------------|-----------------|
| `SERVER_PORT` | Application port | 8080            |

### Database (PostgreSQL)
| Variable            | Description                  | Default/Example                               |
|---------------------|------------------------------|-----------------------------------------------|
| `DB_URL`            | Database connection URL      | jdbc:postgresql://localhost:5433/ohs_database |
| `DB_USERNAME`       | Database username            |                                               |
| `DB_PASSWORD`       | Database password            |                                               |
| `DB_IMAGE`          | Docker image for PostgreSQL  | postgres:16.10-alpine3.21                     |
| `DB_CONTAINER_NAME` | Docker container name for DB |                                               |
| `DB_PORT`           | PostgreSQL port              |                                               |

### Redis
| Variable               | Description                     | Default/Example          |
|------------------------|---------------------------------|--------------------------|
| `REDIS_IMAGE`          | Docker image for Redis          | redis/redis-stack:latest |
| `REDIS_CONTAINER_NAME` | Docker container name for Redis | redis-vector             |
| `REDIS_PORT`           | Redis port                      | 6379                     |
| `REDIS_WEB_PORT`       | Redis web UI port               | 8004                     |

## Contributing

### Submission Guidelines
1. Include clear run instructions
2. Document any design decisions
3. Add comments for complex logic
4. Ensure all tests pass
5. Update README with new features

### Design Notes
- The application uses UUID for entity IDs to ensure uniqueness
- Soft delete pattern is implemented for data retention
- Pagination is implemented for list endpoints
- Response wrapper pattern for consistent API responses

## Contact

**Developer**: David Njau  
**Email**: davidnjau21@gmail.com  
**Website**: https://davidnjau21.com

## License

Proprietary - Internal Use Only  
See: https://davidnjau21.com/licensing
