Digital Health Backend Engineer – One‑Day Story

You’ve just joined a small engineering squad supporting a county hospital that is digitizing its outpatient department. Clinicians struggle to retrieve patient details and past visits quickly, which delays care and creates duplicate records. Your task is to deliver, in a single day, a small but production‑minded service that lets the team record and retrieve core patient information and basic visit data. The service should be easy to run locally, simple to understand, and ready to extend.

On your first morning, the tech lead hands you a minimal Spring Boot starter and a short brief:

“We need a REST API that can manage Patients and their Encounters, and we’d like the structure to leave room for Observations as we expand. Start with the essentials: reliable persistence, predictable validation and errors, and a few targeted queries that mirror how clinicians search in real life.”

What success looks like by end of day
• You can create, read, update, and delete Patients and Encounters.
• You can search Patients by family name, given name, identifier, and birth date (exact or ranges).
• Data is stored using JPA/Hibernate against H2 (or PostgreSQL if you prefer), with a reasonable schema and indexing for common lookups.
• Validation guards required fields and formats (e.g., ISO dates), and errors come back with clear, consistent structures and HTTP status codes.
• There are unit tests for domain/service logic and at least one controller or slice test to exercise the API surface.
• The API is documented via Swagger/OpenAPI or a concise section in your README.

What you’ll likely model first
• Patient: id, identifier, givenName, familyName, birthDate, gender
• Encounter: id, patientId, start, end, encounterClass
• Observation (soon): id, patientId, encounterId?, code, value, effectiveDateTime

How the team will use it tomorrow
• They’ll create Patients and read them back quickly:
  - POST /api/patients
  - GET /api/patients/{id}
  - PUT /api/patients/{id}
  - DELETE /api/patients/{id}
  - GET /api/patients?family=&given=&identifier=&birthDate=
• As capacity allows, they’d love to list a Patient’s Encounters and Observations:
  - GET /api/patients/{id}/encounters
  - GET /api/patients/{id}/observations

Quiet guardrails to keep you on track
• Keep a layered structure (controller → service → repository) with small, readable methods and meaningful names.
• Validate inputs with Bean Validation and return consistent problem details for errors.
• Think about performance: basic indexing for common searches; avoid N+1 where it might bite.

If you have time for stretch goals
• Model the Patient → Encounter → Observation relationships more fully.
• Add pagination, sorting, and date‑range filters for real‑world lists.
• Ship a Dockerfile and one or two integration tests.
• Add a simple API key header for basic auth.

What’s already on your desk
• A minimal Spring Boot starter (Java, Maven, Web, JPA, Validation, H2).

What you’ll hand back
• A working API that meets the core scope.
• Tests that demonstrate correctness of your core logic and API behavior.
• Documentation: how to run it, how to use it, and the key trade‑offs you made.
• Optional: a Dockerfile and any database migration scaffolding you added.

How you (and reviewers) will run it
• Java 17+
• Maven: mvn clean spring-boot:run
• Default port: 8080 (configurable)

How to submit
• Send a Git repository link or a compressed archive with source, a README, and any notes.

How it will be judged
• Correctness, API and data modeling, code quality, validation and error discipline, and how well your choices align with digital health realities.
