package com.roger.service.impl;

import com.roger.domain.Journey;
import com.roger.repository.JourneyRepository;
import com.roger.service.JourneyService;
import com.roger.service.dto.JourneyDTO;
import com.roger.service.mapper.JourneyMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Journey}.
 */
@Service
@Transactional
public class JourneyServiceImpl implements JourneyService {

    private final Logger log = LoggerFactory.getLogger(JourneyServiceImpl.class);

    private final JourneyRepository journeyRepository;

    private final JourneyMapper journeyMapper;

    public JourneyServiceImpl(JourneyRepository journeyRepository, JourneyMapper journeyMapper) {
        this.journeyRepository = journeyRepository;
        this.journeyMapper = journeyMapper;
    }

    @Override
    public JourneyDTO save(JourneyDTO journeyDTO) {
        log.debug("Request to save Journey : {}", journeyDTO);
        Journey journey = journeyMapper.toEntity(journeyDTO);
        journey = journeyRepository.save(journey);
        return journeyMapper.toDto(journey);
    }

    @Override
    public Optional<JourneyDTO> partialUpdate(JourneyDTO journeyDTO) {
        log.debug("Request to partially update Journey : {}", journeyDTO);

        return journeyRepository
            .findById(journeyDTO.getId())
            .map(
                existingJourney -> {
                    journeyMapper.partialUpdate(existingJourney, journeyDTO);
                    return existingJourney;
                }
            )
            .map(journeyRepository::save)
            .map(journeyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<JourneyDTO> findAll() {
        log.debug("Request to get all Journeys");
        return journeyRepository.findAll().stream().map(journeyMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JourneyDTO> findOne(Long id) {
        log.debug("Request to get Journey : {}", id);
        return journeyRepository.findById(id).map(journeyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Journey : {}", id);
        journeyRepository.deleteById(id);
    }
}
