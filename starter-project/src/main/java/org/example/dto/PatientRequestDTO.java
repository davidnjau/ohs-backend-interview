package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record PatientRequestDTO(
        @NotBlank String identifier,
        @NotBlank String givenName,
        @NotBlank String familyName,
        @NotNull @Past LocalDate birthDate,
        @NotBlank String gender
) {}
