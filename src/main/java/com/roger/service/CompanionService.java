package com.roger.service;

import com.roger.service.dto.CompanionDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.roger.domain.Companion}.
 */
public interface CompanionService {
    /**
     * Save a companion.
     *
     * @param companionDTO the entity to save.
     * @return the persisted entity.
     */
    CompanionDTO save(CompanionDTO companionDTO);

    /**
     * Partially updates a companion.
     *
     * @param companionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompanionDTO> partialUpdate(CompanionDTO companionDTO);

    /**
     * Get all the companions.
     *
     * @return the list of entities.
     */
    List<CompanionDTO> findAll();

    /**
     * Get the "id" companion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompanionDTO> findOne(Long id);

    /**
     * Delete the "id" companion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
