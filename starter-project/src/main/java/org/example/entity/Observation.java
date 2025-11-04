package org.example.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "observations",
        indexes = {
                @Index(name = "idx_obs_patient_id", columnList = "patient_id"),
                @Index(name = "idx_obs_encounter_id", columnList = "encounter_id"),
                @Index(name = "idx_obs_code", columnList = "code"),
                @Index(name = "idx_obs_effective_date", columnList = "effective_date_time")
        }
)
public class Observation {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "patient_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_obs_patient")
    )
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "encounter_id",
            foreignKey = @ForeignKey(name = "fk_obs_encounter")
    )
    private Encounter encounter;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 255, name = "observation_value")
    private String observationValue;

    @Column(nullable = false, name = "effective_date_time")
    private LocalDateTime effectiveDateTime;

    // Getters and setters

    public Observation() {}

    public Observation(Patient patient, Encounter encounter, String code, String observationValue, LocalDateTime effectiveDateTime) {
        this.patient = patient;
        this.encounter = encounter;
        this.code = code;
        this.observationValue = observationValue;
        this.effectiveDateTime = effectiveDateTime;
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

    public Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getObservationValue() {
        return observationValue;
    }

    public void setObservationValue(String observationValue) {
        this.observationValue = observationValue;
    }

    public LocalDateTime getEffectiveDateTime() {
        return effectiveDateTime;
    }

    public void setEffectiveDateTime(LocalDateTime effectiveDateTime) {
        this.effectiveDateTime = effectiveDateTime;
    }
}
