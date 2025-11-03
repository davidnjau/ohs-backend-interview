package org.example.mappers;

import org.example.dto.ObservationRequestDTO;
import org.example.dto.ObservationResponseDTO;
import org.example.entity.Observation;
import org.springframework.stereotype.Component;

@Component
public class ObservationMapper {

    public Observation toEntity(ObservationRequestDTO dto) {
        Observation observation = new Observation();
        observation.setPatientId(dto.patientId());
        observation.setEncounterId(dto.encounterId());
        observation.setCode(dto.code());
        observation.setValue(dto.value());
        observation.setEffectiveDateTime(dto.effectiveDateTime());
        return observation;
    }

    public ObservationResponseDTO toDTO(Observation observation) {
        return new ObservationResponseDTO(
                observation.getId(),
                observation.getPatientId(),
                observation.getEncounterId(),
                observation.getCode(),
                observation.getValue(),
                observation.getEffectiveDateTime()
        );
    }
}
