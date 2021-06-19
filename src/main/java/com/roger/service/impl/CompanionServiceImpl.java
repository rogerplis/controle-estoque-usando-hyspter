package com.roger.service.impl;

import com.roger.domain.Companion;
import com.roger.repository.CompanionRepository;
import com.roger.service.CompanionService;
import com.roger.service.dto.CompanionDTO;
import com.roger.service.mapper.CompanionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Companion}.
 */
@Service
@Transactional
public class CompanionServiceImpl implements CompanionService {

    private final Logger log = LoggerFactory.getLogger(CompanionServiceImpl.class);

    private final CompanionRepository companionRepository;

    private final CompanionMapper companionMapper;

    public CompanionServiceImpl(CompanionRepository companionRepository, CompanionMapper companionMapper) {
        this.companionRepository = companionRepository;
        this.companionMapper = companionMapper;
    }

    @Override
    public CompanionDTO save(CompanionDTO companionDTO) {
        log.debug("Request to save Companion : {}", companionDTO);
        Companion companion = companionMapper.toEntity(companionDTO);
        companion = companionRepository.save(companion);
        return companionMapper.toDto(companion);
    }

    @Override
    public Optional<CompanionDTO> partialUpdate(CompanionDTO companionDTO) {
        log.debug("Request to partially update Companion : {}", companionDTO);

        return companionRepository
            .findById(companionDTO.getId())
            .map(
                existingCompanion -> {
                    companionMapper.partialUpdate(existingCompanion, companionDTO);
                    return existingCompanion;
                }
            )
            .map(companionRepository::save)
            .map(companionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanionDTO> findAll() {
        log.debug("Request to get all Companions");
        return companionRepository.findAll().stream().map(companionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompanionDTO> findOne(Long id) {
        log.debug("Request to get Companion : {}", id);
        return companionRepository.findById(id).map(companionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Companion : {}", id);
        companionRepository.deleteById(id);
    }
}
