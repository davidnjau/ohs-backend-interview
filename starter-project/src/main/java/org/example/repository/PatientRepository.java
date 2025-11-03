package org.example.repository;

import org.example.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    @Query("""
        SELECT p FROM Patient p 
        WHERE (:family IS NULL OR LOWER(p.familyName) LIKE LOWER(CONCAT('%', :family, '%')))
        AND (:given IS NULL OR LOWER(p.givenName) LIKE LOWER(CONCAT('%', :given, '%')))
        AND (:identifier IS NULL OR p.identifier = :identifier)
        AND (:birthDate IS NULL OR p.birthDate = :birthDate)
       """)
    Page<Patient> searchPatients(
            @Param("family") String family,
            @Param("given") String given,
            @Param("identifier") String identifier,
            @Param("birthDate") LocalDate birthDate,
            Pageable pageable
    );
}
