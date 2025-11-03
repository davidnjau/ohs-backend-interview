package org.example.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EncounterRequestDTO(
        @NotNull Long patientId,
        @NotNull LocalDateTime start,
        LocalDateTime end,
        @NotNull String encounterClass
) {}
