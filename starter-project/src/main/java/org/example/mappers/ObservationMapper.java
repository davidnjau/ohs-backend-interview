package org.example.mappers;

import org.example.dto.ObservationRequestDTO;
import org.example.dto.ObservationResponseDTO;
import org.example.entity.Encounter;
import org.example.entity.Observation;
import org.example.entity.Patient;
import org.springframework.stereotype.Component;

@Component
public class ObservationMapper {

    public Observation toEntity(ObservationRequestDTO dto, Patient patient, Encounter encounter) {
        Observation observation = new Observation();
        observation.setPatient(patient);
        observation.setEncounter(encounter);
        observation.setCode(dto.code());
        observation.setObservationValue(dto.value());
        observation.setEffectiveDateTime(dto.effectiveDateTime());
        return observation;
    }

    public ObservationResponseDTO toDTO(Observation observation) {
        return new ObservationResponseDTO(
                observation.getId(),
                observation.getPatient().getId(),
                observation.getEncounter().getId(),
                observation.getCode(),
                observation.getObservationValue(),
                observation.getEffectiveDateTime()
        );
    }
}
