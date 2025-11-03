package org.example.repository;

import org.example.entity.Observation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ObservationRepository extends JpaRepository<Observation, UUID> {

    Page<Observation> findByPatientId(UUID patientId, Pageable pageable);
    Page<Observation> findByEncounterId(UUID encounterId, Pageable pageable);
    Page<Observation> findByPatientIdAndEncounterId(UUID patientId, UUID encounterId, Pageable pageable);
}
