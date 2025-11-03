package org.example.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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
    private Long id;

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
}
