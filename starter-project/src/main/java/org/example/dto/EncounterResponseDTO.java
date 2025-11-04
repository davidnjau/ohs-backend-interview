package org.example.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record EncounterResponseDTO(
        UUID id,
        UUID patientId,
        LocalDateTime start,
        LocalDateTime endTime,
        String encounterClass
) {}
