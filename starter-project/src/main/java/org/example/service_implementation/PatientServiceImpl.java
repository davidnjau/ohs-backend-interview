package org.example.service_implementation;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.EncounterResponseDTO;
import org.example.dto.PatientRequestDTO;
import org.example.dto.PatientResponseDTO;
import org.example.entity.Patient;
import org.example.exception.BadRequestException;
import org.example.exception.ConflictException;
import org.example.exception.NotFoundException;
import org.example.mappers.EncounterMapper;
import org.example.mappers.PatientMapper;
import org.example.redis.RedisCacheService;
import org.example.repository.EncounterRepository;
import org.example.repository.PatientRepository;
import org.example.services.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final EncounterRepository encounterRepository;
    private final EncounterMapper encounterMapper;

    private final RedisCacheService redisCacheService;
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    private static final String NAMESPACE = "patient";

    public PatientServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper, EncounterRepository encounterRepository, EncounterMapper encounterMapper, RedisCacheService redisCacheService) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.encounterRepository = encounterRepository;
        this.encounterMapper = encounterMapper;
        this.redisCacheService = redisCacheService;
    }

    private LocalDate parseDate(String birthDate) {
        try {
            return LocalDate.parse(birthDate);
        } catch (DateTimeParseException ex) {
            throw new BadRequestException("Birth date must be a valid date.");
        }
    }


    @Override
    public PatientResponseDTO createPatient(PatientRequestDTO request) {

        String date = request.birthDate();
        LocalDate parsedBirthDate = parseDate(date);

        String indentifier = request.identifier();
        Optional<Patient> patientOptional = patientRepository.findByIdentifier(indentifier);

        if (patientOptional.isPresent()) {
            throw new ConflictException("Identifier already exists");
        }

        Patient patient = patientMapper.toEntity(request);
        patient.setId(UUID.randomUUID());
        patient.setBirthDate(parsedBirthDate);
        patient.setValid(true);

        patient.setEncounters(Collections.emptyList());
        patient.setObservations(Collections.emptyList());

        log.debug("Creating patient: {}", patient);

        PatientResponseDTO dto = patientMapper.toDTO(patientRepository.save(patient));

        // Store in Redis using canonical key + alias
        Map<String, String> aliases = Map.of("identifier", dto.identifier());
        redisCacheService.storeWithAliases(NAMESPACE, dto.id().toString(), dto, aliases, Duration.ofHours(1));

        return dto;
    }

    @Override
    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient not found"));

        String date = request.birthDate();
        LocalDate parsedBirthDate = parseDate(date);

        patient.setIdentifier(request.identifier());
        patient.setGivenName(request.givenName());
        patient.setFamilyName(request.familyName());
        patient.setBirthDate(parsedBirthDate);
        patient.setGender(request.gender());

        PatientResponseDTO dto = patientMapper.toDTO(patientRepository.save(patient));


        // Update Redis cache
        Map<String, String> aliases = Map.of("identifier", dto.identifier());
        redisCacheService.storeWithAliases(NAMESPACE, dto.id().toString(), dto, aliases, Duration.ofHours(1));

        return dto;
    }

    @Override
    public PatientResponseDTO getPatient(UUID id) {

        if (id == null) throw new NotFoundException("Id cannot be null");

        // Try Redis first
        PatientResponseDTO cached = redisCacheService.getWithAliases(NAMESPACE, "uuid", id.toString(), PatientResponseDTO.class);
        if (cached != null) {
            fetchAndCacheEncountersAsync(cached.id()); // fetch encounters in background
            return cached;
        }

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient not found"));

        PatientResponseDTO dto = patientMapper.toDTO(patient);

        // Store in Redis for future access
        Map<String, String> aliases = Map.of("identifier", dto.identifier());
        redisCacheService.storeWithAliases(NAMESPACE, dto.id().toString(), dto, aliases, Duration.ofHours(1));

        fetchAndCacheEncountersAsync(dto.id()); // fetch encounters in background

        return dto;

    }

    @Override
    public void deletePatient(UUID id) {

        if (id == null) {
            throw new NotFoundException("Id cannot be null");
        }
        // Check if the patient exists before deleting it

        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (optionalPatient.isEmpty()) {
            throw new NotFoundException("Patient not found");
        }

        Patient patient = optionalPatient.get();
        patient.setValid(false);

        patientRepository.save(patient);

        // Evict cache
        Map<String, String> aliases = Map.of("identifier", patient.getIdentifier());
        redisCacheService.evictWithAliases(NAMESPACE, id.toString(), aliases);


    }

    /**
     * Asynchronously fetches and caches encounters for a specific patient in Redis.
     * This method submits a background task to retrieve up to 20 encounters associated
     * with the given patient ID and stores them in the Redis cache with a 1-hour expiration.
     * The operation is performed asynchronously to avoid blocking the main thread.
     * If the fetch or cache operation fails, an error is logged without throwing an exception.
     *
     * @param patientId the unique identifier of the patient whose encounters should be fetched and cached;
     *                  must not be null
     */
    private void fetchAndCacheEncountersAsync(UUID patientId) {
        EncounterServiceImpl.fetchAndCacheEncountersAsync(patientId, executor, encounterRepository, encounterMapper, redisCacheService, log);
    }

    /**
     * Searches for patients based on the provided criteria with pagination support.
     * Performs case-insensitive partial matching for family and given names, and exact matching
     * for identifier and birth date. All search parameters are optional.
     *
     * @param family the family name (last name) to search for; supports partial, case-insensitive matching; can be null or empty
     * @param given the given name (first name) to search for; supports partial, case-insensitive matching; can be null or empty
     * @param identifier the unique patient identifier to search for; requires exact match; can be null or empty
     * @param birthDate the patient's birth date to search for; requires exact match; can be null
     * @param page the page number to retrieve (zero-based)
     * @param size the number of results per page
     * @return a list of {@link PatientResponseDTO} objects matching the search criteria for the requested page
     */
    @Override
    public List<PatientResponseDTO> searchPatients(
            String family, String given,
            String identifier, LocalDate birthDate,
            int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Specification<Patient> spec = (root, query, cb) -> {

            // Avoid duplicates due to join fetch
//            assert query != null;
//            query.distinct(true);
//
//            // Fetch child collections in the same query to prevent N+1
//            root.fetch("encounters", JoinType.LEFT);

            Predicate predicate = cb.conjunction(); // start with TRUE

            if (family != null && !family.isEmpty()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("familyName")), "%" + family.toLowerCase() + "%"));
            }
            if (given != null && !given.isEmpty()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("givenName")), "%" + given.toLowerCase() + "%"));
            }
            if (identifier != null && !identifier.isEmpty()) {
                predicate = cb.and(predicate, cb.equal(root.get("identifier"), identifier));
            }
            if (birthDate != null) {
                predicate = cb.and(predicate, cb.equal(root.get("birthDate"), birthDate));
            }

            return predicate;
        };

        Page<Patient> results = patientRepository.findAll(spec, pageable);


        log.debug("Search results: {}", results);
        // Map the Patient entities to PatientResponseDTOs using the patientMapper
        return results.map(patientMapper::toDTO).getContent();

    }

}
