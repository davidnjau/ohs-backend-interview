package org.example.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ObservationResponseDTO(
        UUID id,
        UUID patientId,
        UUID encounterId,
        String code,
        String value,
        LocalDateTime effectiveDateTime
) {}
