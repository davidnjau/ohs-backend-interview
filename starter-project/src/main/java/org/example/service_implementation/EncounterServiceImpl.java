package org.example.service_implementation;

import org.example.entity.Encounter;
import org.example.repository.EncounterRepository;
import org.example.services.EncounterService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EncounterServiceImpl implements EncounterService {

    private final EncounterRepository encounterRepository;

    public EncounterServiceImpl(EncounterRepository encounterRepository) {
        this.encounterRepository = encounterRepository;
    }

    @Override
    public Encounter createEncounter(Encounter encounter) {
        return encounterRepository.save(encounter);
    }

    @Override
    public Optional<Encounter> getEncounter(UUID id) {
        return encounterRepository.findById(id);
    }

    @Override
    public List<Encounter> getEncountersForPatient(UUID patientId) {
        return encounterRepository.findByPatientId(patientId);
    }
}
