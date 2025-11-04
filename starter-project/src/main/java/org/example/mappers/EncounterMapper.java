package org.example.mappers;

import org.example.dto.EncounterRequestDTO;
import org.example.dto.EncounterResponseDTO;
import org.example.entity.Encounter;
import org.example.entity.Patient;
import org.springframework.stereotype.Component;

@Component
public class EncounterMapper {

    public Encounter toEntity(EncounterRequestDTO dto, Patient patient) {
        Encounter encounter = new Encounter();
        encounter.setPatient(patient);
        encounter.setStart(dto.start());
        encounter.setEndTime(dto.endTime());
        encounter.setEncounterClass(dto.encounterClass());
        return encounter;
    }

    public EncounterResponseDTO toDTO(Encounter encounter) {
        return new EncounterResponseDTO(
                encounter.getId(),
                encounter.getPatient().getId(),
                encounter.getStart(),
                encounter.getEndTime(),
                encounter.getEncounterClass()
        );
    }
}
