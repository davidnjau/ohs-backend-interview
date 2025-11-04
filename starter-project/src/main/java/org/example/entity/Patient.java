package org.example.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "patients",
        indexes = {
                @Index(name = "idx_patient_identifier", columnList = "identifier"),
                @Index(name = "idx_patient_family_name", columnList = "family_name"),
                @Index(name = "idx_patient_given_name", columnList = "given_name"),
                @Index(name = "idx_patient_birth_date", columnList = "birth_date")
        }
)
public class Patient {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(unique = true, nullable = false, length = 50)
    private String identifier;

    @Column(name = "given_name", nullable = false, length = 100)
    private String givenName;

    @Column(name = "family_name", nullable = false, length = 100)
    private String familyName;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, length = 20)
    private String gender;

    private boolean valid;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Encounter> encounters = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Observation> observations = new ArrayList<>();

    // Getters and setters


    public Patient() {
    }

    public Patient(String identifier, String givenName, String familyName, LocalDate birthDate, String gender, List<Encounter> encounters, List<Observation> observations) {
        this.identifier = identifier;
        this.givenName = givenName;
        this.familyName = familyName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.encounters = encounters;
        this.observations = observations;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Encounter> getEncounters() {
        return encounters;
    }

    public void setEncounters(List<Encounter> encounters) {
        this.encounters = encounters;
    }

    public List<Observation> getObservations() {
        return observations;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
    }
}
