package org.example.services;

import org.example.dto.EncounterRequestDTO;
import org.example.dto.EncounterResponseDTO;
import org.example.entity.Encounter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EncounterService {

    EncounterResponseDTO createEncounter(EncounterRequestDTO request);

    EncounterResponseDTO getEncounter(UUID id);

    List<EncounterResponseDTO> getEncountersForPatient(UUID patientId, int page, int size);
}
