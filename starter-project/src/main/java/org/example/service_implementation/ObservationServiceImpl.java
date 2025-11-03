package org.example.service_implementation;

import org.example.entity.Observation;
import org.example.repository.ObservationRepository;
import org.example.services.ObservationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ObservationServiceImpl implements ObservationService {

    private final ObservationRepository observationRepository;

    public ObservationServiceImpl(ObservationRepository observationRepository) {
        this.observationRepository = observationRepository;
    }

    @Override
    public Observation createObservation(Observation observation) {
        return observationRepository.save(observation);
    }

    @Override
    public Optional<Observation> getObservation(UUID id) {
        return observationRepository.findById(id);
    }

    @Override
    public List<Observation> getObservationsForPatient(UUID patientId) {
        return observationRepository.findByPatientId(patientId);
    }

    @Override
    public List<Observation> getObservationsForEncounter(UUID encounterId) {
        return observationRepository.findByEncounterId(encounterId);
    }
}
