package org.example.service_implementation;

import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.PatientRequestDTO;
import org.example.dto.PatientResponseDTO;
import org.example.entity.Patient;
import org.example.exception.BadRequestException;
import org.example.exception.NotFoundException;
import org.example.mappers.PatientMapper;
import org.example.repository.PatientRepository;
import org.example.services.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    private LocalDate parseDate(String birthDate) {
        try {
            return LocalDate.parse(birthDate);
        } catch (DateTimeParseException ex) {
            throw new BadRequestException("Birth date must be a valid date.");
        }
    }


    @Override
    public PatientResponseDTO createPatient(PatientRequestDTO request) {

        String date = request.birthDate();
        LocalDate parsedBirthDate = parseDate(date);

        Patient patient = patientMapper.toEntity(request);
        patient.setId(UUID.randomUUID());
        patient.setBirthDate(parsedBirthDate);
        patient.setValid(true);

        patient.setEncounters(Collections.emptyList());
        patient.setObservations(Collections.emptyList());

        log.debug("Creating patient: {}", patient);

        return patientMapper.toDTO(patientRepository.save(patient));
    }

    @Override
    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient not found"));

        String date = request.birthDate();
        LocalDate parsedBirthDate = parseDate(date);

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

        Specification<Patient> spec = (root, query, cb) -> {
            Predicate predicate = cb.conjunction(); // start with TRUE

            if (family != null && !family.isEmpty()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("familyName")), "%" + family.toLowerCase() + "%"));
            }
            if (given != null && !given.isEmpty()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("givenName")), "%" + given.toLowerCase() + "%"));
            }
            if (identifier != null && !identifier.isEmpty()) {
                predicate = cb.and(predicate, cb.equal(root.get("identifier"), identifier));
            }
            if (birthDate != null) {
                predicate = cb.and(predicate, cb.equal(root.get("birthDate"), birthDate));
            }

            return predicate;
        };

        Page<Patient> results = patientRepository.findAll(spec, pageable);
        log.debug("Search results: {}", results);
        // Map the Patient entities to PatientResponseDTOs using the patientMapper
        return results.map(patientMapper::toDTO).getContent();

    }

}
