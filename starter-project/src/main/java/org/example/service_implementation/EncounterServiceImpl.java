package org.example.service_implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.EncounterRequestDTO;
import org.example.dto.EncounterResponseDTO;
import org.example.entity.Encounter;
import org.example.entity.Patient;
import org.example.exception.NotFoundException;
import org.example.mappers.EncounterMapper;
import org.example.redis.RedisCacheService;
import org.example.repository.EncounterRepository;
import org.example.repository.PatientRepository;
import org.example.services.EncounterService;
import org.slf4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class EncounterServiceImpl implements EncounterService {

    private final EncounterRepository encounterRepository;
    private final EncounterMapper encounterMapper;
    private final PatientRepository patientRepository;
    private final RedisCacheService redisCacheService;
    private final ExecutorService executor = Executors.newFixedThreadPool(2);


    public EncounterServiceImpl(EncounterRepository encounterRepository, EncounterMapper encounterMapper, PatientRepository patientRepository, RedisCacheService redisCacheService) {
        this.encounterRepository = encounterRepository;
        this.encounterMapper = encounterMapper;
        this.patientRepository = patientRepository;
        this.redisCacheService = redisCacheService;
    }

    @Override
    public EncounterResponseDTO createEncounter(EncounterRequestDTO request) {

        // Validate patient exists before creating encounter
        Optional<Patient> optionalPatient = patientRepository.findById(request.patientId());
        if (optionalPatient.isEmpty()) {
            throw new NotFoundException("Patient not found");
        }
        Patient patient = optionalPatient.get();

        Encounter encounter = encounterMapper.toEntity(request, patient);
        encounter.setId(UUID.randomUUID());
        return encounterMapper.toDTO(encounterRepository.save(encounter));
    }

    @Override
    public EncounterResponseDTO getEncounter(UUID id) {

        Optional<Encounter> encounterOptional = encounterRepository.findById(id);
        if (encounterOptional.isEmpty()) {
            throw new NotFoundException("Encounter not found");
        }

        return encounterMapper.toDTO(encounterOptional.get());

    }

    @Override
    public List<EncounterResponseDTO> getEncountersForPatient(UUID patientId, int page, int size) {

        // Verify patient exists and is valid
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new NotFoundException("Patient not found"));

        if (!patient.isValid()) {
            throw new NotFoundException("Encounters for this patient are not available. Patient is not valid. Please contact the administrator.");
        }

        // Redis key for cached encounters
        String redisKey = "patient:encounters:" + patientId + ":page:" + page + ":size:" + size;

        // Try Redis first
        List<EncounterResponseDTO> cachedEncounters = redisCacheService.getValue(redisKey, List.class);
        if (cachedEncounters != null) {
            log.debug("Returning {} encounters for patient {} from cache", cachedEncounters.size(), patientId);
            return cachedEncounters;
        }

        // Fetch from DB
        List<EncounterResponseDTO> encounters = encounterRepository.findByPatientId(patientId, PageRequest.of(page, size))
                .stream()
                .map(encounterMapper::toDTO)
                .toList();

        // Cache in Redis for future requests
        if (!encounters.isEmpty()) {
            fetchAndCacheEncountersAsync(patientId); // fetch encounters in background
        }

        return encounters;
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
        fetchAndCacheEncountersAsync(patientId, executor, encounterRepository, encounterMapper, redisCacheService, log);
    }

    static void fetchAndCacheEncountersAsync(
            UUID patientId,
            ExecutorService executor,
            EncounterRepository encounterRepository,
            EncounterMapper encounterMapper,
            RedisCacheService redisCacheService,
            Logger log
    ) {
        executor.submit(() -> {
            try {
                // Fetch first page of encounters from DB
                List<EncounterResponseDTO> encounters = encounterRepository
                        .findByPatientId(patientId, PageRequest.of(0, 20))
                        .stream()
                        .map(encounterMapper::toDTO)
                        .toList();

                if (!encounters.isEmpty()) {
                    // Redis key for first page
                    String redisKey = "patient:encounters:" + patientId + ":page:0:size:20";

                    // Store entire page as a list in Redis
                    redisCacheService.setValueWithTime(redisKey, encounters, 1, TimeUnit.HOURS);

                    log.debug("Cached {} encounters for patient {} under key [{}]", encounters.size(), patientId, redisKey);
                }
            } catch (Exception e) {
                log.error("Failed to fetch/cache encounters for patient {}", patientId, e);
            }
        });
    }


}
