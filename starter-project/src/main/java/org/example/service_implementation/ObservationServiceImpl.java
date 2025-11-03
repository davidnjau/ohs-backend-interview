package org.example.service_implementation;

import org.example.dto.ObservationRequestDTO;
import org.example.dto.ObservationResponseDTO;
import org.example.entity.Observation;
import org.example.entity.Patient;
import org.example.exception.NotFoundException;
import org.example.mappers.ObservationMapper;
import org.example.repository.EncounterRepository;
import org.example.repository.ObservationRepository;
import org.example.repository.PatientRepository;
import org.example.services.ObservationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ObservationServiceImpl implements ObservationService {

    private final PatientRepository patientRepository;
    private final EncounterRepository encounterRepository;
    private final ObservationRepository observationRepository;
    private final ObservationMapper observationMapper;

    public ObservationServiceImpl(PatientRepository patientRepository, EncounterRepository encounterRepository, ObservationRepository observationRepository, ObservationMapper observationMapper) {
        this.patientRepository = patientRepository;
        this.encounterRepository = encounterRepository;
        this.observationRepository = observationRepository;
        this.observationMapper = observationMapper;
    }

    @Override
    public ObservationResponseDTO createObservation(ObservationRequestDTO observation) {

        // Validate patient and encounter exist before creating observation
        boolean patientExists = patientRepository.existsById(observation.patientId());
        if (!patientExists) {
            throw new NotFoundException("Patient not found");
        }

        boolean encounterExists = encounterRepository.existsById(observation.encounterId());
        if (!encounterExists) {
            throw new NotFoundException("Encounter not found");
        }

        Observation observationSave = new Observation(
                patientRepository.findById(observation.patientId()).get(),
                encounterRepository.findById(observation.encounterId()).get(),
                observation.code(),
                observation.value(),
                observation.effectiveDateTime());
        observationRepository.save(observationSave);

        return observationMapper.toDTO(observationSave);

    }

    @Override
    public ObservationResponseDTO getObservation(UUID id) {

        if (id == null) {
            throw new NotFoundException("Observation id is required");
        }
        Optional<Observation> observationOptional = observationRepository.findById(id);
        if (observationOptional.isEmpty()) {
            throw new NotFoundException("Observation not found");
        }
        return observationMapper.toDTO(observationOptional.get());

    }

    @Override
    public List<ObservationResponseDTO> getObservationsForPatient(UUID patientId, int page, int size) {

        if (patientId == null) {
            throw new NotFoundException("Patient id is required");
        }

        // Validate patient and encounter exist before creating observation
        Optional<Patient> optionalPatient = patientRepository.findById(patientId);
        if (optionalPatient.isEmpty()) {
            throw new NotFoundException("Patient not found");
        }
        Patient patient = optionalPatient.get();
        if (!patient.isValid()) {
            throw new NotFoundException("Observations for this patient are not available. Patient is not valid. Please contact the administrator.  ");
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Observation> pageableObservations = observationRepository.findByPatientId(patientId, pageable);
        List<Observation> observations = pageableObservations.getContent();
        return observations.stream().map(observationMapper::toDTO).toList();

    }

    @Override
    public List<ObservationResponseDTO> getObservationsForEncounter(UUID encounterId, int page, int size) {

        if (encounterId == null) {
            throw new NotFoundException("Encounter id is required");
        }

        boolean encounterExists = encounterRepository.existsById(encounterId);
        if (!encounterExists) {
            throw new NotFoundException("Encounter not found");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Observation> pageableObservations = observationRepository.findByEncounterId(encounterId, pageable);
        List<Observation> observations = pageableObservations.getContent();
        return observations.stream().map(observationMapper::toDTO).toList();

    }
}
