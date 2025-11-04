package org.example.services;

import org.example.dto.*;
import org.example.entity.Patient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientService {

    PatientResponseDTO createPatient(PatientRequestDTO request);

    PatientResponseDTO updatePatient(UUID id, PatientRequestDTO request);

    PatientResponseDTO getPatient(UUID id);

    void deletePatient(UUID id);

    List<PatientResponseDTO> searchPatients(
            String family, String given, String identifier, LocalDate birthDate, int page, int size
    );



}
