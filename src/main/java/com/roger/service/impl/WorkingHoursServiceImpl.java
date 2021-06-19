package com.roger.service.impl;

import com.roger.domain.WorkingHours;
import com.roger.repository.WorkingHoursRepository;
import com.roger.service.WorkingHoursService;
import com.roger.service.dto.WorkingHoursDTO;
import com.roger.service.mapper.WorkingHoursMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link WorkingHours}.
 */
@Service
@Transactional
public class WorkingHoursServiceImpl implements WorkingHoursService {

    private final Logger log = LoggerFactory.getLogger(WorkingHoursServiceImpl.class);

    private final WorkingHoursRepository workingHoursRepository;

    private final WorkingHoursMapper workingHoursMapper;

    public WorkingHoursServiceImpl(WorkingHoursRepository workingHoursRepository, WorkingHoursMapper workingHoursMapper) {
        this.workingHoursRepository = workingHoursRepository;
        this.workingHoursMapper = workingHoursMapper;
    }

    @Override
    public WorkingHoursDTO save(WorkingHoursDTO workingHoursDTO) {
        log.debug("Request to save WorkingHours : {}", workingHoursDTO);
        WorkingHours workingHours = workingHoursMapper.toEntity(workingHoursDTO);
        workingHours = workingHoursRepository.save(workingHours);
        return workingHoursMapper.toDto(workingHours);
    }

    @Override
    public Optional<WorkingHoursDTO> partialUpdate(WorkingHoursDTO workingHoursDTO) {
        log.debug("Request to partially update WorkingHours : {}", workingHoursDTO);

        return workingHoursRepository
            .findById(workingHoursDTO.getId())
            .map(
                existingWorkingHours -> {
                    workingHoursMapper.partialUpdate(existingWorkingHours, workingHoursDTO);
                    return existingWorkingHours;
                }
            )
            .map(workingHoursRepository::save)
            .map(workingHoursMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkingHoursDTO> findAll() {
        log.debug("Request to get all WorkingHours");
        return workingHoursRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(workingHoursMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<WorkingHoursDTO> findAllWithEagerRelationships(Pageable pageable) {
        return workingHoursRepository.findAllWithEagerRelationships(pageable).map(workingHoursMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkingHoursDTO> findOne(Long id) {
        log.debug("Request to get WorkingHours : {}", id);
        return workingHoursRepository.findOneWithEagerRelationships(id).map(workingHoursMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete WorkingHours : {}", id);
        workingHoursRepository.deleteById(id);
    }
}
