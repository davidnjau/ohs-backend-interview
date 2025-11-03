package org.example.controllers;

import org.example.entity.Observation;
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
    public ResponseEntity<Observation> createObservation(@RequestBody Observation observation) {
        return ResponseEntity.ok(observationService.createObservation(observation));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Observation> getObservation(@PathVariable UUID id) {
        return observationService.getObservation(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Observation>> getObservationsForPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(observationService.getObservationsForPatient(patientId));
    }

    @GetMapping("/encounter/{encounterId}")
    public ResponseEntity<List<Observation>> getObservationsForEncounter(@PathVariable UUID encounterId) {
        return ResponseEntity.ok(observationService.getObservationsForEncounter(encounterId));
    }
}
