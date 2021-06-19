package com.roger.service;

import com.roger.service.dto.JourneyDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.roger.domain.Journey}.
 */
public interface JourneyService {
    /**
     * Save a journey.
     *
     * @param journeyDTO the entity to save.
     * @return the persisted entity.
     */
    JourneyDTO save(JourneyDTO journeyDTO);

    /**
     * Partially updates a journey.
     *
     * @param journeyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<JourneyDTO> partialUpdate(JourneyDTO journeyDTO);

    /**
     * Get all the journeys.
     *
     * @return the list of entities.
     */
    List<JourneyDTO> findAll();

    /**
     * Get the "id" journey.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<JourneyDTO> findOne(Long id);

    /**
     * Delete the "id" journey.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
