package org.example.dto;

import java.time.LocalDateTime;

public record EncounterResponseDTO(
        Long id,
        Long patientId,
        LocalDateTime start,
        LocalDateTime end,
        String encounterClass
) {}
