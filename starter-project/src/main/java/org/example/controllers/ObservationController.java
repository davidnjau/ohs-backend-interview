package org.example.controllers;

import org.example.dto.ObservationRequestDTO;
import org.example.dto.ObservationResponseDTO;
import org.example.entity.Observation;
import org.example.exception.ResponseWrapper;
import org.example.services.ObservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/observations")
public class ObservationController {

    private final ObservationService observationService;

    public ObservationController(ObservationService observationService) {
        this.observationService = observationService;
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<ObservationResponseDTO>> createObservation(@RequestBody ObservationRequestDTO observation) {
        return ResponseEntity.ok(
                ResponseWrapper.success(observationService.createObservation(observation))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<ObservationResponseDTO>> getObservation(@PathVariable UUID id) {

        ObservationResponseDTO observation = observationService.getObservation(id);
        return ResponseEntity.ok(
                ResponseWrapper.success(observation)
        );


    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ResponseWrapper<List<ObservationResponseDTO>>> getObservationsForPatient(
            @PathVariable UUID patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size

    ) {

        return ResponseEntity.ok(
                ResponseWrapper.success(observationService.getObservationsForPatient(patientId, page, size))
        );

    }

    @GetMapping("/encounter/{encounterId}")
    public ResponseEntity<ResponseWrapper<List<ObservationResponseDTO>>> getObservationsForEncounter(
            @PathVariable UUID encounterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.success(observationService.getObservationsForPatient(encounterId, page, size))
        );
    }
    @GetMapping("/encounter/{patientId}/{encounterId}")
    public ResponseEntity<ResponseWrapper<List<ObservationResponseDTO>>> getObservationsForEncounterAndPatients(
            @PathVariable UUID patientId,
            @PathVariable UUID encounterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(
                ResponseWrapper.success(observationService.getObservationsForEncounterAndPatients(patientId, encounterId, page, size))
        );
    }
}
