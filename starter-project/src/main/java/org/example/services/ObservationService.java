package org.example.services;

import org.example.dto.ObservationRequestDTO;
import org.example.dto.ObservationResponseDTO;
import org.example.entity.Observation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ObservationService {

    ObservationResponseDTO createObservation(ObservationRequestDTO observation);

    ObservationResponseDTO getObservation(UUID id);

    List<ObservationResponseDTO> getObservationsForPatient(UUID patientId, int page, int size);

    List<ObservationResponseDTO> getObservationsForEncounter(UUID encounterId, int page, int size);
}
