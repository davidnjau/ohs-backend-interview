package org.example.dto;

import java.time.LocalDateTime;

public record ObservationResponseDTO(
        Long id,
        Long patientId,
        Long encounterId,
        String code,
        String value,
        LocalDateTime effectiveDateTime
) {}
