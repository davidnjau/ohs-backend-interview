package org.example.controllers;

import org.example.entity.Patient;
import org.example.exception.ResponseWrapper;
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

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<Patient>> createPatient(@RequestBody Patient patient) {
        return ResponseEntity.ok(
                ResponseWrapper
                        .success(patientService.createPatient(patient)
                        )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable UUID id) {

        

        return patientService.getPatient(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable UUID id, @RequestBody Patient patient) {
        return ResponseEntity.ok(patientService.updatePatient(id, patient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Patient>> searchPatients(
            @RequestParam(required = false) String family,
            @RequestParam(required = false) String given,
            @RequestParam(required = false) String identifier,
            @RequestParam(required = false) LocalDate birthDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(
                patientService.searchPatients(family, given, identifier, birthDate)
        );
    }
}
