package org.example.controllers;

import jakarta.validation.Valid;
import org.example.dto.EncounterResponseDTO;
import org.example.dto.ObservationResponseDTO;
import org.example.dto.PatientRequestDTO;
import org.example.dto.PatientResponseDTO;
import org.example.entity.Patient;
import org.example.exception.NotFoundException;
import org.example.exception.ResponseWrapper;
import org.example.services.EncounterService;
import org.example.services.ObservationService;
import org.example.services.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;
    private final EncounterService encounterService;
    private final ObservationService observationService;

    public PatientController(PatientService patientService, EncounterService encounterService, ObservationService observationService) {
        this.patientService = patientService;
        this.encounterService = encounterService;
        this.observationService = observationService;
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<PatientResponseDTO>> createPatient(@RequestBody PatientRequestDTO patient) {
        return ResponseEntity.ok(
                ResponseWrapper
                        .success(patientService.createPatient(patient))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<PatientResponseDTO>> getPatient(@PathVariable UUID id) {

        PatientResponseDTO patient = patientService.getPatient(id);
        return ResponseEntity.ok(
                ResponseWrapper.success(patient)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseWrapper<PatientResponseDTO>> updatePatient(
            @PathVariable UUID id,
            @RequestBody PatientRequestDTO patient) {

        PatientResponseDTO updatedPatient = patientService.updatePatient(id, patient);
        return ResponseEntity.ok(
                ResponseWrapper.success(updatedPatient)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseWrapper<String>> deletePatient(@PathVariable UUID id) {

        patientService.deletePatient(id);
        return ResponseEntity.ok(
                ResponseWrapper.success("Patient deactivated successfully")
        );
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<PatientResponseDTO>>> searchPatients(
            @RequestParam(required = false) String family,
            @RequestParam(required = false) String given,
            @RequestParam(required = false) String identifier,
            @RequestParam(required = false) LocalDate birthDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        List<PatientResponseDTO> patients = patientService.searchPatients(
                family, given, identifier, birthDate, page, size);

        return ResponseEntity.ok(
                ResponseWrapper.success(patients)
        );
    }

    @GetMapping("/{patientId}/encounters")
    public ResponseEntity<ResponseWrapper<List<EncounterResponseDTO>>> getEncounters(
            @PathVariable UUID patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
            ) {

        List<EncounterResponseDTO> encounters = encounterService.getEncountersForPatient(patientId, page, size);
        return ResponseEntity.ok(
                ResponseWrapper.success(encounters)
        );

    }

    @GetMapping("/{patientId}/observations")
    public ResponseEntity<ResponseWrapper<List<ObservationResponseDTO>>> getObservations(
            @PathVariable UUID patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        return ResponseEntity.ok(
                ResponseWrapper.success(observationService.getObservationsForPatient(patientId, page, size))
        );
    }

}
