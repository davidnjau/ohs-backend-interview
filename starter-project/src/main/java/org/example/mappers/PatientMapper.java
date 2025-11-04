package org.example.mappers;

import org.example.dto.PatientRequestDTO;
import org.example.dto.PatientResponseDTO;
import org.example.entity.Patient;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PatientMapper {


    public Patient toEntity(PatientRequestDTO dto) {
        Patient patient = new Patient();
        patient.setIdentifier(dto.identifier());
        patient.setGivenName(dto.givenName());
        patient.setFamilyName(dto.familyName());
        patient.setGender(dto.gender());
        return patient;
    }

    public PatientResponseDTO toDTO(Patient patient) {
        return new PatientResponseDTO(
                patient.getId(),
                patient.getIdentifier(),
                patient.getGivenName(),
                patient.getFamilyName(),
                patient.getBirthDate(),
                patient.getGender(),
                patient.isValid()
        );
    }
}
