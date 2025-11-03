package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "observations",
        indexes = {
                @Index(name = "idx_obs_patient_id", columnList = "patient_id"),
                @Index(name = "idx_obs_encounter_id", columnList = "encounter_id"),
                @Index(name = "idx_obs_code", columnList = "code"),
                @Index(name = "idx_obs_effective_date", columnList = "effectiveDateTime")
        }
)
public class Observation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "encounter_id")
    private Encounter encounter;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 255)
    private String value;

    @Column(nullable = false)
    private LocalDateTime effectiveDateTime;

    // Getters and setters

    public Observation() {}

    public Observation(Patient patient, Encounter encounter, String code, String value, LocalDateTime effectiveDateTime) {
        this.patient = patient;
        this.encounter = encounter;
        this.code = code;
        this.value = value;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getEffectiveDateTime() {
        return effectiveDateTime;
    }

    public void setEffectiveDateTime(LocalDateTime effectiveDateTime) {
        this.effectiveDateTime = effectiveDateTime;
    }
}
