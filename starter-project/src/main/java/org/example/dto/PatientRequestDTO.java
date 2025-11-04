package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PatientRequestDTO(
        @NotBlank String identifier,
        @NotBlank String givenName,
        @NotBlank String familyName,

        @NotNull
        String birthDate,

        @NotBlank
        @Pattern(
                regexp = "^(Male|Female)$",
                message = "Gender must be Male or Female"
        )
        String gender
) {}
