package org.example.service_implementation;

import org.example.entity.Patient;
import org.example.repository.PatientRepository;
import org.example.services.PatientService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient createPatient(Patient patient) {

        // Validate input data before saving to the database


        return patientRepository.save(patient);
    }

    @Override
    public Optional<Patient> getPatient(UUID id) {



        return patientRepository.findById(id);
    }

    @Override
    public Patient updatePatient(UUID id, Patient updated) {
        return patientRepository.findById(id)
                .map(existing -> {
                    existing.setIdentifier(updated.getIdentifier());
                    existing.setGivenName(updated.getGivenName());
                    existing.setFamilyName(updated.getFamilyName());
                    existing.setBirthDate(updated.getBirthDate());
                    existing.setGender(updated.getGender());
                    return patientRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    @Override
    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }

    @Override
    public List<Patient> searchPatients(String familyName, String givenName, String identifier, LocalDate birthDate) {
        if (identifier != null) return patientRepository.findByIdentifier(identifier);
        if (familyName != null) return patientRepository.findByFamilyNameContainingIgnoreCase(familyName);
        if (givenName != null) return patientRepository.findByGivenNameContainingIgnoreCase(givenName);
        if (birthDate != null) return patientRepository.findByBirthDate(birthDate);
        return patientRepository.findAll();
    }
}
