package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ObservationRequestDTO(
        @NotNull Long patientId,
        Long encounterId,
        @NotBlank String code,
        @NotBlank String value,
        @NotNull LocalDateTime effectiveDateTime
) {}
