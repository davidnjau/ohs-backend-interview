OHS Digital Health Backend Exercise – Starter

Prerequisites
- Java 17+
- Maven 3.8+

Getting Started
1. Navigate to starter-project
2. Run: mvn clean spring-boot:run
3. API runs at http://localhost:8080

Tech Stack
- Spring Boot (Web, Data JPA, Validation)
- H2 database (in‑memory)

Configuration
- See starter-project/src/main/resources/application.properties
- Change DB or port as needed

Development Notes
- Use layered architecture (controller → service → repository)
- Prefer DTOs at API boundary
- Add Bean Validation annotations and exception handling

API Documentation
- Option A: Add springdoc‑openapi‑starter‑webmvc‑ui and expose Swagger UI at /swagger-ui.html
- Option B: Document endpoints with examples in README or EXERCISE.md

Testing
- JUnit 5 and Spring Boot test starter are available once added to pom
- Include unit tests for services and one controller/slice test

Submission
- Include run instructions and any design notes in your project README

