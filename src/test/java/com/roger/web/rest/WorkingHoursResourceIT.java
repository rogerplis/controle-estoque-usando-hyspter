package com.roger.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.roger.IntegrationTest;
import com.roger.domain.WorkingHours;
import com.roger.domain.enumeration.Days;
import com.roger.repository.WorkingHoursRepository;
import com.roger.service.WorkingHoursService;
import com.roger.service.dto.WorkingHoursDTO;
import com.roger.service.mapper.WorkingHoursMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WorkingHoursResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WorkingHoursResourceIT {

    private static final LocalDate DEFAULT_ENTRY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENTRY = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_LEAVING_WORK = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LEAVING_WORK = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXTRA_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXTRA_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXTRA_TIME_2 = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXTRA_TIME_2 = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ENTRY_REST = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENTRY_REST = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_RETURN_REST = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RETURN_REST = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final LocalDate DEFAULT_DAY_WEEK = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DAY_WEEK = LocalDate.now(ZoneId.systemDefault());

    private static final Days DEFAULT_DAY = Days.SEGUNDA;
    private static final Days UPDATED_DAY = Days.TERCA;

    private static final String ENTITY_API_URL = "/api/working-hours";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkingHoursRepository workingHoursRepository;

    @Mock
    private WorkingHoursRepository workingHoursRepositoryMock;

    @Autowired
    private WorkingHoursMapper workingHoursMapper;

    @Mock
    private WorkingHoursService workingHoursServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkingHoursMockMvc;

    private WorkingHours workingHours;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkingHours createEntity(EntityManager em) {
        WorkingHours workingHours = new WorkingHours()
            .entry(DEFAULT_ENTRY)
            .leavingWork(DEFAULT_LEAVING_WORK)
            .extraTime(DEFAULT_EXTRA_TIME)
            .extraTime2(DEFAULT_EXTRA_TIME_2)
            .entryRest(DEFAULT_ENTRY_REST)
            .returnRest(DEFAULT_RETURN_REST)
            .dayWeek(DEFAULT_DAY_WEEK)
            .day(DEFAULT_DAY);
        return workingHours;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkingHours createUpdatedEntity(EntityManager em) {
        WorkingHours workingHours = new WorkingHours()
            .entry(UPDATED_ENTRY)
            .leavingWork(UPDATED_LEAVING_WORK)
            .extraTime(UPDATED_EXTRA_TIME)
            .extraTime2(UPDATED_EXTRA_TIME_2)
            .entryRest(UPDATED_ENTRY_REST)
            .returnRest(UPDATED_RETURN_REST)
            .dayWeek(UPDATED_DAY_WEEK)
            .day(UPDATED_DAY);
        return workingHours;
    }

    @BeforeEach
    public void initTest() {
        workingHours = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkingHours() throws Exception {
        int databaseSizeBeforeCreate = workingHoursRepository.findAll().size();
        // Create the WorkingHours
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(workingHours);
        restWorkingHoursMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workingHoursDTO))
            )
            .andExpect(status().isCreated());

        // Validate the WorkingHours in the database
        List<WorkingHours> workingHoursList = workingHoursRepository.findAll();
        assertThat(workingHoursList).hasSize(databaseSizeBeforeCreate + 1);
        WorkingHours testWorkingHours = workingHoursList.get(workingHoursList.size() - 1);
        assertThat(testWorkingHours.getEntry()).isEqualTo(DEFAULT_ENTRY);
        assertThat(testWorkingHours.getLeavingWork()).isEqualTo(DEFAULT_LEAVING_WORK);
        assertThat(testWorkingHours.getExtraTime()).isEqualTo(DEFAULT_EXTRA_TIME);
        assertThat(testWorkingHours.getExtraTime2()).isEqualTo(DEFAULT_EXTRA_TIME_2);
        assertThat(testWorkingHours.getEntryRest()).isEqualTo(DEFAULT_ENTRY_REST);
        assertThat(testWorkingHours.getReturnRest()).isEqualTo(DEFAULT_RETURN_REST);
        assertThat(testWorkingHours.getDayWeek()).isEqualTo(DEFAULT_DAY_WEEK);
        assertThat(testWorkingHours.getDay()).isEqualTo(DEFAULT_DAY);
    }

    @Test
    @Transactional
    void createWorkingHoursWithExistingId() throws Exception {
        // Create the WorkingHours with an existing ID
        workingHours.setId(1L);
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(workingHours);

        int databaseSizeBeforeCreate = workingHoursRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkingHoursMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workingHoursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingHours in the database
        List<WorkingHours> workingHoursList = workingHoursRepository.findAll();
        assertThat(workingHoursList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWorkingHours() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        // Get all the workingHoursList
        restWorkingHoursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workingHours.getId().intValue())))
            .andExpect(jsonPath("$.[*].entry").value(hasItem(DEFAULT_ENTRY.toString())))
            .andExpect(jsonPath("$.[*].leavingWork").value(hasItem(DEFAULT_LEAVING_WORK.toString())))
            .andExpect(jsonPath("$.[*].extraTime").value(hasItem(DEFAULT_EXTRA_TIME.toString())))
            .andExpect(jsonPath("$.[*].extraTime2").value(hasItem(DEFAULT_EXTRA_TIME_2.toString())))
            .andExpect(jsonPath("$.[*].entryRest").value(hasItem(DEFAULT_ENTRY_REST.toString())))
            .andExpect(jsonPath("$.[*].returnRest").value(hasItem(DEFAULT_RETURN_REST.toString())))
            .andExpect(jsonPath("$.[*].dayWeek").value(hasItem(DEFAULT_DAY_WEEK.toString())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkingHoursWithEagerRelationshipsIsEnabled() throws Exception {
        when(workingHoursServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkingHoursMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(workingHoursServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWorkingHoursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(workingHoursServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWorkingHoursMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(workingHoursServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getWorkingHours() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        // Get the workingHours
        restWorkingHoursMockMvc
            .perform(get(ENTITY_API_URL_ID, workingHours.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workingHours.getId().intValue()))
            .andExpect(jsonPath("$.entry").value(DEFAULT_ENTRY.toString()))
            .andExpect(jsonPath("$.leavingWork").value(DEFAULT_LEAVING_WORK.toString()))
            .andExpect(jsonPath("$.extraTime").value(DEFAULT_EXTRA_TIME.toString()))
            .andExpect(jsonPath("$.extraTime2").value(DEFAULT_EXTRA_TIME_2.toString()))
            .andExpect(jsonPath("$.entryRest").value(DEFAULT_ENTRY_REST.toString()))
            .andExpect(jsonPath("$.returnRest").value(DEFAULT_RETURN_REST.toString()))
            .andExpect(jsonPath("$.dayWeek").value(DEFAULT_DAY_WEEK.toString()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWorkingHours() throws Exception {
        // Get the workingHours
        restWorkingHoursMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkingHours() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        int databaseSizeBeforeUpdate = workingHoursRepository.findAll().size();

        // Update the workingHours
        WorkingHours updatedWorkingHours = workingHoursRepository.findById(workingHours.getId()).get();
        // Disconnect from session so that the updates on updatedWorkingHours are not directly saved in db
        em.detach(updatedWorkingHours);
        updatedWorkingHours
            .entry(UPDATED_ENTRY)
            .leavingWork(UPDATED_LEAVING_WORK)
            .extraTime(UPDATED_EXTRA_TIME)
            .extraTime2(UPDATED_EXTRA_TIME_2)
            .entryRest(UPDATED_ENTRY_REST)
            .returnRest(UPDATED_RETURN_REST)
            .dayWeek(UPDATED_DAY_WEEK)
            .day(UPDATED_DAY);
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(updatedWorkingHours);

        restWorkingHoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workingHoursDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workingHoursDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkingHours in the database
        List<WorkingHours> workingHoursList = workingHoursRepository.findAll();
        assertThat(workingHoursList).hasSize(databaseSizeBeforeUpdate);
        WorkingHours testWorkingHours = workingHoursList.get(workingHoursList.size() - 1);
        assertThat(testWorkingHours.getEntry()).isEqualTo(UPDATED_ENTRY);
        assertThat(testWorkingHours.getLeavingWork()).isEqualTo(UPDATED_LEAVING_WORK);
        assertThat(testWorkingHours.getExtraTime()).isEqualTo(UPDATED_EXTRA_TIME);
        assertThat(testWorkingHours.getExtraTime2()).isEqualTo(UPDATED_EXTRA_TIME_2);
        assertThat(testWorkingHours.getEntryRest()).isEqualTo(UPDATED_ENTRY_REST);
        assertThat(testWorkingHours.getReturnRest()).isEqualTo(UPDATED_RETURN_REST);
        assertThat(testWorkingHours.getDayWeek()).isEqualTo(UPDATED_DAY_WEEK);
        assertThat(testWorkingHours.getDay()).isEqualTo(UPDATED_DAY);
    }

    @Test
    @Transactional
    void putNonExistingWorkingHours() throws Exception {
        int databaseSizeBeforeUpdate = workingHoursRepository.findAll().size();
        workingHours.setId(count.incrementAndGet());

        // Create the WorkingHours
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(workingHours);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkingHoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workingHoursDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workingHoursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingHours in the database
        List<WorkingHours> workingHoursList = workingHoursRepository.findAll();
        assertThat(workingHoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkingHours() throws Exception {
        int databaseSizeBeforeUpdate = workingHoursRepository.findAll().size();
        workingHours.setId(count.incrementAndGet());

        // Create the WorkingHours
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(workingHours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkingHoursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workingHoursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingHours in the database
        List<WorkingHours> workingHoursList = workingHoursRepository.findAll();
        assertThat(workingHoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkingHours() throws Exception {
        int databaseSizeBeforeUpdate = workingHoursRepository.findAll().size();
        workingHours.setId(count.incrementAndGet());

        // Create the WorkingHours
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(workingHours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkingHoursMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(workingHoursDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkingHours in the database
        List<WorkingHours> workingHoursList = workingHoursRepository.findAll();
        assertThat(workingHoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkingHoursWithPatch() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        int databaseSizeBeforeUpdate = workingHoursRepository.findAll().size();

        // Update the workingHours using partial update
        WorkingHours partialUpdatedWorkingHours = new WorkingHours();
        partialUpdatedWorkingHours.setId(workingHours.getId());

        partialUpdatedWorkingHours
            .entry(UPDATED_ENTRY)
            .leavingWork(UPDATED_LEAVING_WORK)
            .extraTime(UPDATED_EXTRA_TIME)
            .dayWeek(UPDATED_DAY_WEEK)
            .day(UPDATED_DAY);

        restWorkingHoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkingHours.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkingHours))
            )
            .andExpect(status().isOk());

        // Validate the WorkingHours in the database
        List<WorkingHours> workingHoursList = workingHoursRepository.findAll();
        assertThat(workingHoursList).hasSize(databaseSizeBeforeUpdate);
        WorkingHours testWorkingHours = workingHoursList.get(workingHoursList.size() - 1);
        assertThat(testWorkingHours.getEntry()).isEqualTo(UPDATED_ENTRY);
        assertThat(testWorkingHours.getLeavingWork()).isEqualTo(UPDATED_LEAVING_WORK);
        assertThat(testWorkingHours.getExtraTime()).isEqualTo(UPDATED_EXTRA_TIME);
        assertThat(testWorkingHours.getExtraTime2()).isEqualTo(DEFAULT_EXTRA_TIME_2);
        assertThat(testWorkingHours.getEntryRest()).isEqualTo(DEFAULT_ENTRY_REST);
        assertThat(testWorkingHours.getReturnRest()).isEqualTo(DEFAULT_RETURN_REST);
        assertThat(testWorkingHours.getDayWeek()).isEqualTo(UPDATED_DAY_WEEK);
        assertThat(testWorkingHours.getDay()).isEqualTo(UPDATED_DAY);
    }

    @Test
    @Transactional
    void fullUpdateWorkingHoursWithPatch() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        int databaseSizeBeforeUpdate = workingHoursRepository.findAll().size();

        // Update the workingHours using partial update
        WorkingHours partialUpdatedWorkingHours = new WorkingHours();
        partialUpdatedWorkingHours.setId(workingHours.getId());

        partialUpdatedWorkingHours
            .entry(UPDATED_ENTRY)
            .leavingWork(UPDATED_LEAVING_WORK)
            .extraTime(UPDATED_EXTRA_TIME)
            .extraTime2(UPDATED_EXTRA_TIME_2)
            .entryRest(UPDATED_ENTRY_REST)
            .returnRest(UPDATED_RETURN_REST)
            .dayWeek(UPDATED_DAY_WEEK)
            .day(UPDATED_DAY);

        restWorkingHoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkingHours.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkingHours))
            )
            .andExpect(status().isOk());

        // Validate the WorkingHours in the database
        List<WorkingHours> workingHoursList = workingHoursRepository.findAll();
        assertThat(workingHoursList).hasSize(databaseSizeBeforeUpdate);
        WorkingHours testWorkingHours = workingHoursList.get(workingHoursList.size() - 1);
        assertThat(testWorkingHours.getEntry()).isEqualTo(UPDATED_ENTRY);
        assertThat(testWorkingHours.getLeavingWork()).isEqualTo(UPDATED_LEAVING_WORK);
        assertThat(testWorkingHours.getExtraTime()).isEqualTo(UPDATED_EXTRA_TIME);
        assertThat(testWorkingHours.getExtraTime2()).isEqualTo(UPDATED_EXTRA_TIME_2);
        assertThat(testWorkingHours.getEntryRest()).isEqualTo(UPDATED_ENTRY_REST);
        assertThat(testWorkingHours.getReturnRest()).isEqualTo(UPDATED_RETURN_REST);
        assertThat(testWorkingHours.getDayWeek()).isEqualTo(UPDATED_DAY_WEEK);
        assertThat(testWorkingHours.getDay()).isEqualTo(UPDATED_DAY);
    }

    @Test
    @Transactional
    void patchNonExistingWorkingHours() throws Exception {
        int databaseSizeBeforeUpdate = workingHoursRepository.findAll().size();
        workingHours.setId(count.incrementAndGet());

        // Create the WorkingHours
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(workingHours);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkingHoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workingHoursDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workingHoursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingHours in the database
        List<WorkingHours> workingHoursList = workingHoursRepository.findAll();
        assertThat(workingHoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkingHours() throws Exception {
        int databaseSizeBeforeUpdate = workingHoursRepository.findAll().size();
        workingHours.setId(count.incrementAndGet());

        // Create the WorkingHours
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(workingHours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkingHoursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workingHoursDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkingHours in the database
        List<WorkingHours> workingHoursList = workingHoursRepository.findAll();
        assertThat(workingHoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkingHours() throws Exception {
        int databaseSizeBeforeUpdate = workingHoursRepository.findAll().size();
        workingHours.setId(count.incrementAndGet());

        // Create the WorkingHours
        WorkingHoursDTO workingHoursDTO = workingHoursMapper.toDto(workingHours);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkingHoursMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workingHoursDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkingHours in the database
        List<WorkingHours> workingHoursList = workingHoursRepository.findAll();
        assertThat(workingHoursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkingHours() throws Exception {
        // Initialize the database
        workingHoursRepository.saveAndFlush(workingHours);

        int databaseSizeBeforeDelete = workingHoursRepository.findAll().size();

        // Delete the workingHours
        restWorkingHoursMockMvc
            .perform(delete(ENTITY_API_URL_ID, workingHours.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkingHours> workingHoursList = workingHoursRepository.findAll();
        assertThat(workingHoursList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
