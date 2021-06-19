package com.roger.service;

import com.roger.service.dto.WorkingHoursDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.roger.domain.WorkingHours}.
 */
public interface WorkingHoursService {
    /**
     * Save a workingHours.
     *
     * @param workingHoursDTO the entity to save.
     * @return the persisted entity.
     */
    WorkingHoursDTO save(WorkingHoursDTO workingHoursDTO);

    /**
     * Partially updates a workingHours.
     *
     * @param workingHoursDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<WorkingHoursDTO> partialUpdate(WorkingHoursDTO workingHoursDTO);

    /**
     * Get all the workingHours.
     *
     * @return the list of entities.
     */
    List<WorkingHoursDTO> findAll();

    /**
     * Get all the workingHours with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<WorkingHoursDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" workingHours.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<WorkingHoursDTO> findOne(Long id);

    /**
     * Delete the "id" workingHours.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
