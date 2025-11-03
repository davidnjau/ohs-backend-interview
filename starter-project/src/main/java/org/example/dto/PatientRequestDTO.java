package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record PatientRequestDTO(
        @NotBlank String identifier,
        @NotBlank String givenName,
        @NotBlank String familyName,

        @NotNull
        @Past
        @Pattern(
                regexp = "^\\d{4}-\\d{2}-\\d{2}$",
                message = "Birth date must be in ISO format YYYY-MM-DD"
        )
        String birthDate,

        @NotBlank
        @Pattern(
                regexp = "^(Male|Female)$",
                message = "Gender must be Male or Female"
        )
        String gender
) {}
