package org.example.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "encounters",
        indexes = {
                @Index(name = "idx_encounter_patient_id", columnList = "patient_id"),
                @Index(name = "idx_encounter_start", columnList = "start"),
                @Index(name = "idx_encounter_class", columnList = "encounter_class")
        }
)
public class Encounter {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "patient_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_encounter_patient")
    )
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime start;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "encounter_class", nullable = false, length = 50)
    private String encounterClass;

    @OneToMany(mappedBy = "encounter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Observation> observations = new ArrayList<>();

    // Getters and setters


    public Encounter() {
    }

    public Encounter(Patient patient, LocalDateTime start, LocalDateTime endTime, String encounterClass, List<Observation> observations) {
        this.patient = patient;
        this.start = start;
        this.endTime = endTime;
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

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
