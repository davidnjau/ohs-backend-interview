package org.example.dto;

import java.time.LocalDate;
import java.util.UUID;

public record PatientResponseDTO(
        UUID id,
        String identifier,
        String givenName,
        String familyName,
        LocalDate birthDate,
        String gender
) {}
