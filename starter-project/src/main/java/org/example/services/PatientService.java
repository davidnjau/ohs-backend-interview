package org.example.services;

import org.example.entity.Patient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientService {

    Patient createPatient(Patient patient);

    Optional<Patient> getPatient(UUID id);

    Patient updatePatient(UUID id, Patient patient);

    void deletePatient(UUID id);

    List<Patient> searchPatients(String familyName, String givenName, String identifier, LocalDate birthDate);
}
