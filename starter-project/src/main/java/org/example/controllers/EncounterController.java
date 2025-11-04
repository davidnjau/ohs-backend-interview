package org.example.controllers;

import org.example.dto.EncounterRequestDTO;
import org.example.dto.EncounterResponseDTO;
import org.example.entity.Encounter;
import org.example.exception.ResponseWrapper;
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
    public ResponseEntity<ResponseWrapper<EncounterResponseDTO>> createEncounter(@RequestBody EncounterRequestDTO encounter) {
        return ResponseEntity.ok(
                ResponseWrapper.success(encounterService.createEncounter(encounter))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseWrapper<EncounterResponseDTO>> getEncounter(@PathVariable UUID id) {

        EncounterResponseDTO encounter = encounterService.getEncounter(id);
        return ResponseEntity.ok(
                ResponseWrapper.success(encounter)
        );
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ResponseWrapper<List<EncounterResponseDTO>>> getEncountersForPatient(
            @PathVariable UUID patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
            ) {
        List<EncounterResponseDTO> encounters = encounterService.getEncountersForPatient(patientId, page, size);
        return ResponseEntity.ok(
                ResponseWrapper.success(encounters)
        );
    }
}
