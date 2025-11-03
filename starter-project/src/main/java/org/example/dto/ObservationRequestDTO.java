package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record ObservationRequestDTO(
        @NotNull UUID patientId,
        @NotNull UUID encounterId,
        @NotBlank String code,
        @NotBlank String value,
        @NotNull LocalDateTime effectiveDateTime
) {}
