package org.example.services;

import org.example.entity.Observation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ObservationService {

    Observation createObservation(Observation observation);

    Optional<Observation> getObservation(UUID id);

    List<Observation> getObservationsForPatient(UUID patientId);

    List<Observation> getObservationsForEncounter(UUID encounterId);
}
