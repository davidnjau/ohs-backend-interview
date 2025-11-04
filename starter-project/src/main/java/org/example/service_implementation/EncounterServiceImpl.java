package org.example.service_implementation;

import org.example.dto.EncounterRequestDTO;
import org.example.dto.EncounterResponseDTO;
import org.example.entity.Encounter;
import org.example.entity.Patient;
import org.example.exception.NotFoundException;
import org.example.mappers.EncounterMapper;
import org.example.repository.EncounterRepository;
import org.example.repository.PatientRepository;
import org.example.services.EncounterService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EncounterServiceImpl implements EncounterService {

    private final EncounterRepository encounterRepository;
    private final EncounterMapper encounterMapper;
    private final PatientRepository patientRepository;


    public EncounterServiceImpl(EncounterRepository encounterRepository, EncounterMapper encounterMapper, PatientRepository patientRepository) {
        this.encounterRepository = encounterRepository;
        this.encounterMapper = encounterMapper;
        this.patientRepository = patientRepository;
    }

    @Override
    public EncounterResponseDTO createEncounter(EncounterRequestDTO request) {

        // Validate patient exists before creating encounter
        Optional<Patient> optionalPatient = patientRepository.findById(request.patientId());
        if (optionalPatient.isEmpty()) {
            throw new NotFoundException("Patient not found");
        }
        Patient patient = optionalPatient.get();

        Encounter encounter = encounterMapper.toEntity(request, patient);
        encounter.setId(UUID.randomUUID());
        return encounterMapper.toDTO(encounterRepository.save(encounter));
    }

    @Override
    public EncounterResponseDTO getEncounter(UUID id) {

        Optional<Encounter> encounterOptional = encounterRepository.findById(id);
        if (encounterOptional.isEmpty()) {
            throw new NotFoundException("Encounter not found");
        }

        return encounterMapper.toDTO(encounterOptional.get());

    }

    @Override
    public List<EncounterResponseDTO> getEncountersForPatient(UUID patientId, int page, int size) {

        Optional<Patient> optionalPatient = patientRepository.findById(patientId);
        if (optionalPatient.isEmpty()) {
            throw new NotFoundException("Patient not found");
        }
        Patient patient = optionalPatient.get();
        if (!patient.isValid()) {
            throw new NotFoundException("Encounters for this patient are not available. Patient is not valid. Please contact the administrator.  ");
        }

        return encounterRepository.findByPatientId(patientId, PageRequest.of(page, size))
                .stream().map(encounterMapper::toDTO).toList();
    }


}
