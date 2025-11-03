package org.example.dto;

import java.time.LocalDate;

public record PatientResponseDTO(
        Long id,
        String identifier,
        String givenName,
        String familyName,
        LocalDate birthDate,
        String gender
) {}
