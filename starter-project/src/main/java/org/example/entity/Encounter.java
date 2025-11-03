package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "encounters",
        indexes = {
                @Index(name = "idx_encounter_patient_id", columnList = "patient_id"),
                @Index(name = "idx_encounter_start", columnList = "start"),
                @Index(name = "idx_encounter_class", columnList = "encounterClass")
        }
)
public class Encounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime start;

    private LocalDateTime end;

    @Column(nullable = false, length = 50)
    private String encounterClass;

    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Observation> observations;

    // Getters and setters


    public Encounter() {
    }

    public Encounter(Patient patient, LocalDateTime start, LocalDateTime end, String encounterClass, List<Observation> observations) {
        this.patient = patient;
        this.start = start;
        this.end = end;
        this.encounterClass = encounterClass;
        this.observations = observations;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getEncounterClass() {
        return encounterClass;
    }

    public void setEncounterClass(String encounterClass) {
        this.encounterClass = encounterClass;
    }

    public List<Observation> getObservations() {
        return observations;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
    }
}
