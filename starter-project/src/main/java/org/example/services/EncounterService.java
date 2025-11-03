package org.example.services;

import org.example.entity.Encounter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EncounterService {

    Encounter createEncounter(Encounter encounter);

    Optional<Encounter> getEncounter(UUID id);

    List<Encounter> getEncountersForPatient(UUID patientId);
}
