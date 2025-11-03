package org.example.repository;

import org.example.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    List<Patient> findByFamilyNameContainingIgnoreCase(String familyName);
    List<Patient> findByGivenNameContainingIgnoreCase(String givenName);
    List<Patient> findByIdentifier(String identifier);
    List<Patient> findByBirthDate(LocalDate birthDate);
}
