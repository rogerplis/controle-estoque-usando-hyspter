package com.roger.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.roger.IntegrationTest;
import com.roger.domain.Journey;
import com.roger.repository.JourneyRepository;
import com.roger.service.dto.JourneyDTO;
import com.roger.service.mapper.JourneyMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link JourneyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class JourneyResourceIT {

    private static final String DEFAULT_JOURNEY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_JOURNEY_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOLERANCE = 1;
    private static final Integer UPDATED_TOLERANCE = 2;

    private static final Instant DEFAULT_START_JOURNEY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_JOURNEY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_JOURNEY = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_JOURNEY = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_DAY_OUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DAY_OUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/journeys";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JourneyRepository journeyRepository;

    @Autowired
    private JourneyMapper journeyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJourneyMockMvc;

    private Journey journey;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Journey createEntity(EntityManager em) {
        Journey journey = new Journey()
            .journeyName(DEFAULT_JOURNEY_NAME)
            .tolerance(DEFAULT_TOLERANCE)
            .startJourney(DEFAULT_START_JOURNEY)
            .endJourney(DEFAULT_END_JOURNEY)
            .dayOut(DEFAULT_DAY_OUT)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return journey;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Journey createUpdatedEntity(EntityManager em) {
        Journey journey = new Journey()
            .journeyName(UPDATED_JOURNEY_NAME)
            .tolerance(UPDATED_TOLERANCE)
            .startJourney(UPDATED_START_JOURNEY)
            .endJourney(UPDATED_END_JOURNEY)
            .dayOut(UPDATED_DAY_OUT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return journey;
    }

    @BeforeEach
    public void initTest() {
        journey = createEntity(em);
    }

    @Test
    @Transactional
    void createJourney() throws Exception {
        int databaseSizeBeforeCreate = journeyRepository.findAll().size();
        // Create the Journey
        JourneyDTO journeyDTO = journeyMapper.toDto(journey);
        restJourneyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(journeyDTO)))
            .andExpect(status().isCreated());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeCreate + 1);
        Journey testJourney = journeyList.get(journeyList.size() - 1);
        assertThat(testJourney.getJourneyName()).isEqualTo(DEFAULT_JOURNEY_NAME);
        assertThat(testJourney.getTolerance()).isEqualTo(DEFAULT_TOLERANCE);
        assertThat(testJourney.getStartJourney()).isEqualTo(DEFAULT_START_JOURNEY);
        assertThat(testJourney.getEndJourney()).isEqualTo(DEFAULT_END_JOURNEY);
        assertThat(testJourney.getDayOut()).isEqualTo(DEFAULT_DAY_OUT);
        assertThat(testJourney.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testJourney.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void createJourneyWithExistingId() throws Exception {
        // Create the Journey with an existing ID
        journey.setId(1L);
        JourneyDTO journeyDTO = journeyMapper.toDto(journey);

        int databaseSizeBeforeCreate = journeyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJourneyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(journeyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllJourneys() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        // Get all the journeyList
        restJourneyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(journey.getId().intValue())))
            .andExpect(jsonPath("$.[*].journeyName").value(hasItem(DEFAULT_JOURNEY_NAME)))
            .andExpect(jsonPath("$.[*].tolerance").value(hasItem(DEFAULT_TOLERANCE)))
            .andExpect(jsonPath("$.[*].startJourney").value(hasItem(DEFAULT_START_JOURNEY.toString())))
            .andExpect(jsonPath("$.[*].endJourney").value(hasItem(DEFAULT_END_JOURNEY.toString())))
            .andExpect(jsonPath("$.[*].dayOut").value(hasItem(DEFAULT_DAY_OUT.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    void getJourney() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        // Get the journey
        restJourneyMockMvc
            .perform(get(ENTITY_API_URL_ID, journey.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(journey.getId().intValue()))
            .andExpect(jsonPath("$.journeyName").value(DEFAULT_JOURNEY_NAME))
            .andExpect(jsonPath("$.tolerance").value(DEFAULT_TOLERANCE))
            .andExpect(jsonPath("$.startJourney").value(DEFAULT_START_JOURNEY.toString()))
            .andExpect(jsonPath("$.endJourney").value(DEFAULT_END_JOURNEY.toString()))
            .andExpect(jsonPath("$.dayOut").value(DEFAULT_DAY_OUT.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingJourney() throws Exception {
        // Get the journey
        restJourneyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewJourney() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        int databaseSizeBeforeUpdate = journeyRepository.findAll().size();

        // Update the journey
        Journey updatedJourney = journeyRepository.findById(journey.getId()).get();
        // Disconnect from session so that the updates on updatedJourney are not directly saved in db
        em.detach(updatedJourney);
        updatedJourney
            .journeyName(UPDATED_JOURNEY_NAME)
            .tolerance(UPDATED_TOLERANCE)
            .startJourney(UPDATED_START_JOURNEY)
            .endJourney(UPDATED_END_JOURNEY)
            .dayOut(UPDATED_DAY_OUT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        JourneyDTO journeyDTO = journeyMapper.toDto(updatedJourney);

        restJourneyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, journeyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(journeyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeUpdate);
        Journey testJourney = journeyList.get(journeyList.size() - 1);
        assertThat(testJourney.getJourneyName()).isEqualTo(UPDATED_JOURNEY_NAME);
        assertThat(testJourney.getTolerance()).isEqualTo(UPDATED_TOLERANCE);
        assertThat(testJourney.getStartJourney()).isEqualTo(UPDATED_START_JOURNEY);
        assertThat(testJourney.getEndJourney()).isEqualTo(UPDATED_END_JOURNEY);
        assertThat(testJourney.getDayOut()).isEqualTo(UPDATED_DAY_OUT);
        assertThat(testJourney.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testJourney.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void putNonExistingJourney() throws Exception {
        int databaseSizeBeforeUpdate = journeyRepository.findAll().size();
        journey.setId(count.incrementAndGet());

        // Create the Journey
        JourneyDTO journeyDTO = journeyMapper.toDto(journey);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJourneyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, journeyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(journeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJourney() throws Exception {
        int databaseSizeBeforeUpdate = journeyRepository.findAll().size();
        journey.setId(count.incrementAndGet());

        // Create the Journey
        JourneyDTO journeyDTO = journeyMapper.toDto(journey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJourneyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(journeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJourney() throws Exception {
        int databaseSizeBeforeUpdate = journeyRepository.findAll().size();
        journey.setId(count.incrementAndGet());

        // Create the Journey
        JourneyDTO journeyDTO = journeyMapper.toDto(journey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJourneyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(journeyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJourneyWithPatch() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        int databaseSizeBeforeUpdate = journeyRepository.findAll().size();

        // Update the journey using partial update
        Journey partialUpdatedJourney = new Journey();
        partialUpdatedJourney.setId(journey.getId());

        partialUpdatedJourney.startJourney(UPDATED_START_JOURNEY).endJourney(UPDATED_END_JOURNEY).startDate(UPDATED_START_DATE);

        restJourneyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJourney.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJourney))
            )
            .andExpect(status().isOk());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeUpdate);
        Journey testJourney = journeyList.get(journeyList.size() - 1);
        assertThat(testJourney.getJourneyName()).isEqualTo(DEFAULT_JOURNEY_NAME);
        assertThat(testJourney.getTolerance()).isEqualTo(DEFAULT_TOLERANCE);
        assertThat(testJourney.getStartJourney()).isEqualTo(UPDATED_START_JOURNEY);
        assertThat(testJourney.getEndJourney()).isEqualTo(UPDATED_END_JOURNEY);
        assertThat(testJourney.getDayOut()).isEqualTo(DEFAULT_DAY_OUT);
        assertThat(testJourney.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testJourney.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void fullUpdateJourneyWithPatch() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        int databaseSizeBeforeUpdate = journeyRepository.findAll().size();

        // Update the journey using partial update
        Journey partialUpdatedJourney = new Journey();
        partialUpdatedJourney.setId(journey.getId());

        partialUpdatedJourney
            .journeyName(UPDATED_JOURNEY_NAME)
            .tolerance(UPDATED_TOLERANCE)
            .startJourney(UPDATED_START_JOURNEY)
            .endJourney(UPDATED_END_JOURNEY)
            .dayOut(UPDATED_DAY_OUT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restJourneyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJourney.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJourney))
            )
            .andExpect(status().isOk());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeUpdate);
        Journey testJourney = journeyList.get(journeyList.size() - 1);
        assertThat(testJourney.getJourneyName()).isEqualTo(UPDATED_JOURNEY_NAME);
        assertThat(testJourney.getTolerance()).isEqualTo(UPDATED_TOLERANCE);
        assertThat(testJourney.getStartJourney()).isEqualTo(UPDATED_START_JOURNEY);
        assertThat(testJourney.getEndJourney()).isEqualTo(UPDATED_END_JOURNEY);
        assertThat(testJourney.getDayOut()).isEqualTo(UPDATED_DAY_OUT);
        assertThat(testJourney.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testJourney.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingJourney() throws Exception {
        int databaseSizeBeforeUpdate = journeyRepository.findAll().size();
        journey.setId(count.incrementAndGet());

        // Create the Journey
        JourneyDTO journeyDTO = journeyMapper.toDto(journey);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJourneyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, journeyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(journeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJourney() throws Exception {
        int databaseSizeBeforeUpdate = journeyRepository.findAll().size();
        journey.setId(count.incrementAndGet());

        // Create the Journey
        JourneyDTO journeyDTO = journeyMapper.toDto(journey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJourneyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(journeyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJourney() throws Exception {
        int databaseSizeBeforeUpdate = journeyRepository.findAll().size();
        journey.setId(count.incrementAndGet());

        // Create the Journey
        JourneyDTO journeyDTO = journeyMapper.toDto(journey);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJourneyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(journeyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Journey in the database
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJourney() throws Exception {
        // Initialize the database
        journeyRepository.saveAndFlush(journey);

        int databaseSizeBeforeDelete = journeyRepository.findAll().size();

        // Delete the journey
        restJourneyMockMvc
            .perform(delete(ENTITY_API_URL_ID, journey.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Journey> journeyList = journeyRepository.findAll();
        assertThat(journeyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
