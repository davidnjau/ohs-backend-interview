package org.example.controllers;

import org.example.entity.Encounter;
import org.example.services.EncounterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/encounters")
public class EncounterController {

    private final EncounterService encounterService;

    public EncounterController(EncounterService encounterService) {
        this.encounterService = encounterService;
    }

    @PostMapping
    public ResponseEntity<Encounter> createEncounter(@RequestBody Encounter encounter) {
        return ResponseEntity.ok(encounterService.createEncounter(encounter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Encounter> getEncounter(@PathVariable UUID id) {
        return encounterService.getEncounter(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Encounter>> getEncountersForPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(encounterService.getEncountersForPatient(patientId));
    }
}
