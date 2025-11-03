package org.example.service_implementation;

import org.example.dto.PatientRequestDTO;
import org.example.dto.PatientResponseDTO;
import org.example.entity.Patient;
import org.example.exception.NotFoundException;
import org.example.mappers.PatientMapper;
import org.example.repository.PatientRepository;
import org.example.services.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    @Override
    public PatientResponseDTO createPatient(PatientRequestDTO request) {
        Patient patient = patientMapper.toEntity(request);
        return patientMapper.toDTO(patientRepository.save(patient));
    }

    @Override
    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient not found"));

        LocalDate parsedBirthDate = LocalDate.parse(request.birthDate());

        patient.setIdentifier(request.identifier());
        patient.setGivenName(request.givenName());
        patient.setFamilyName(request.familyName());
        patient.setBirthDate(parsedBirthDate);
        patient.setGender(request.gender());

        return patientMapper.toDTO(patientRepository.save(patient));
    }

    @Override
    public PatientResponseDTO getPatient(UUID id) {

        if (id == null) {
            throw new NotFoundException("Id cannot be null");
        }

        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (optionalPatient.isEmpty()) {
            throw new NotFoundException("Patient not found");
        }

        return patientMapper.toDTO(optionalPatient.get());

    }

    @Override
    public void deletePatient(UUID id) {

        if (id == null) {
            throw new NotFoundException("Id cannot be null");
        }
        // Check if the patient exists before deleting it

        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (optionalPatient.isEmpty()) {
            throw new NotFoundException("Patient not found");
        }

        Patient patient = optionalPatient.get();
        patient.setValid(false);

        patientRepository.save(patient);



    }

    @Override
    public List<PatientResponseDTO> searchPatients(
            String family, String given,
            String identifier, LocalDate birthDate,
            int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Patient> results = patientRepository.searchPatients(
                family,
                given,
                identifier,
                birthDate,
                pageable
        );

        Page<PatientResponseDTO> pageableResults = results.map(patientMapper::toDTO);

        // Convert the Page<T> to a List<T> using getContent()
        return pageableResults.getContent();
    }

}
