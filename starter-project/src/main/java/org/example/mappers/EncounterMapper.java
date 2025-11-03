package org.example.mappers;

import org.example.dto.EncounterRequestDTO;
import org.example.dto.EncounterResponseDTO;
import org.example.entity.Encounter;
import org.springframework.stereotype.Component;

@Component
public class EncounterMapper {

    public Encounter toEntity(EncounterRequestDTO dto) {
        Encounter encounter = new Encounter();
        encounter.setPatientId(dto.patientId());
        encounter.setStart(dto.start());
        encounter.setEnd(dto.end());
        encounter.setEncounterClass(dto.encounterClass());
        return encounter;
    }

    public EncounterResponseDTO toDTO(Encounter encounter) {
        return new EncounterResponseDTO(
                encounter.getId(),
                encounter.getPatientId(),
                encounter.getStart(),
                encounter.getEnd(),
                encounter.getEncounterClass()
        );
    }
}
