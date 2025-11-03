package org.example.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record EncounterRequestDTO(
        @NotNull UUID patientId,
        @NotNull LocalDateTime start,
        LocalDateTime end,
        @NotNull String encounterClass
) {}
