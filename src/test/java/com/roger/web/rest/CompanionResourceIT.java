package com.roger.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.roger.IntegrationTest;
import com.roger.domain.Companion;
import com.roger.repository.CompanionRepository;
import com.roger.service.dto.CompanionDTO;
import com.roger.service.mapper.CompanionMapper;
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
 * Integration tests for the {@link CompanionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompanionResourceIT {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CNPJ = "AAAAAAAAAA";
    private static final String UPDATED_CNPJ = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/companions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompanionRepository companionRepository;

    @Autowired
    private CompanionMapper companionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanionMockMvc;

    private Companion companion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Companion createEntity(EntityManager em) {
        Companion companion = new Companion().companyName(DEFAULT_COMPANY_NAME).cnpj(DEFAULT_CNPJ);
        return companion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Companion createUpdatedEntity(EntityManager em) {
        Companion companion = new Companion().companyName(UPDATED_COMPANY_NAME).cnpj(UPDATED_CNPJ);
        return companion;
    }

    @BeforeEach
    public void initTest() {
        companion = createEntity(em);
    }

    @Test
    @Transactional
    void createCompanion() throws Exception {
        int databaseSizeBeforeCreate = companionRepository.findAll().size();
        // Create the Companion
        CompanionDTO companionDTO = companionMapper.toDto(companion);
        restCompanionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companionDTO)))
            .andExpect(status().isCreated());

        // Validate the Companion in the database
        List<Companion> companionList = companionRepository.findAll();
        assertThat(companionList).hasSize(databaseSizeBeforeCreate + 1);
        Companion testCompanion = companionList.get(companionList.size() - 1);
        assertThat(testCompanion.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testCompanion.getCnpj()).isEqualTo(DEFAULT_CNPJ);
    }

    @Test
    @Transactional
    void createCompanionWithExistingId() throws Exception {
        // Create the Companion with an existing ID
        companion.setId(1L);
        CompanionDTO companionDTO = companionMapper.toDto(companion);

        int databaseSizeBeforeCreate = companionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Companion in the database
        List<Companion> companionList = companionRepository.findAll();
        assertThat(companionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompanions() throws Exception {
        // Initialize the database
        companionRepository.saveAndFlush(companion);

        // Get all the companionList
        restCompanionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companion.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)));
    }

    @Test
    @Transactional
    void getCompanion() throws Exception {
        // Initialize the database
        companionRepository.saveAndFlush(companion);

        // Get the companion
        restCompanionMockMvc
            .perform(get(ENTITY_API_URL_ID, companion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(companion.getId().intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ));
    }

    @Test
    @Transactional
    void getNonExistingCompanion() throws Exception {
        // Get the companion
        restCompanionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompanion() throws Exception {
        // Initialize the database
        companionRepository.saveAndFlush(companion);

        int databaseSizeBeforeUpdate = companionRepository.findAll().size();

        // Update the companion
        Companion updatedCompanion = companionRepository.findById(companion.getId()).get();
        // Disconnect from session so that the updates on updatedCompanion are not directly saved in db
        em.detach(updatedCompanion);
        updatedCompanion.companyName(UPDATED_COMPANY_NAME).cnpj(UPDATED_CNPJ);
        CompanionDTO companionDTO = companionMapper.toDto(updatedCompanion);

        restCompanionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Companion in the database
        List<Companion> companionList = companionRepository.findAll();
        assertThat(companionList).hasSize(databaseSizeBeforeUpdate);
        Companion testCompanion = companionList.get(companionList.size() - 1);
        assertThat(testCompanion.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testCompanion.getCnpj()).isEqualTo(UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void putNonExistingCompanion() throws Exception {
        int databaseSizeBeforeUpdate = companionRepository.findAll().size();
        companion.setId(count.incrementAndGet());

        // Create the Companion
        CompanionDTO companionDTO = companionMapper.toDto(companion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, companionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Companion in the database
        List<Companion> companionList = companionRepository.findAll();
        assertThat(companionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompanion() throws Exception {
        int databaseSizeBeforeUpdate = companionRepository.findAll().size();
        companion.setId(count.incrementAndGet());

        // Create the Companion
        CompanionDTO companionDTO = companionMapper.toDto(companion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(companionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Companion in the database
        List<Companion> companionList = companionRepository.findAll();
        assertThat(companionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompanion() throws Exception {
        int databaseSizeBeforeUpdate = companionRepository.findAll().size();
        companion.setId(count.incrementAndGet());

        // Create the Companion
        CompanionDTO companionDTO = companionMapper.toDto(companion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(companionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Companion in the database
        List<Companion> companionList = companionRepository.findAll();
        assertThat(companionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanionWithPatch() throws Exception {
        // Initialize the database
        companionRepository.saveAndFlush(companion);

        int databaseSizeBeforeUpdate = companionRepository.findAll().size();

        // Update the companion using partial update
        Companion partialUpdatedCompanion = new Companion();
        partialUpdatedCompanion.setId(companion.getId());

        restCompanionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanion))
            )
            .andExpect(status().isOk());

        // Validate the Companion in the database
        List<Companion> companionList = companionRepository.findAll();
        assertThat(companionList).hasSize(databaseSizeBeforeUpdate);
        Companion testCompanion = companionList.get(companionList.size() - 1);
        assertThat(testCompanion.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testCompanion.getCnpj()).isEqualTo(DEFAULT_CNPJ);
    }

    @Test
    @Transactional
    void fullUpdateCompanionWithPatch() throws Exception {
        // Initialize the database
        companionRepository.saveAndFlush(companion);

        int databaseSizeBeforeUpdate = companionRepository.findAll().size();

        // Update the companion using partial update
        Companion partialUpdatedCompanion = new Companion();
        partialUpdatedCompanion.setId(companion.getId());

        partialUpdatedCompanion.companyName(UPDATED_COMPANY_NAME).cnpj(UPDATED_CNPJ);

        restCompanionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompanion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompanion))
            )
            .andExpect(status().isOk());

        // Validate the Companion in the database
        List<Companion> companionList = companionRepository.findAll();
        assertThat(companionList).hasSize(databaseSizeBeforeUpdate);
        Companion testCompanion = companionList.get(companionList.size() - 1);
        assertThat(testCompanion.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testCompanion.getCnpj()).isEqualTo(UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void patchNonExistingCompanion() throws Exception {
        int databaseSizeBeforeUpdate = companionRepository.findAll().size();
        companion.setId(count.incrementAndGet());

        // Create the Companion
        CompanionDTO companionDTO = companionMapper.toDto(companion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, companionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Companion in the database
        List<Companion> companionList = companionRepository.findAll();
        assertThat(companionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompanion() throws Exception {
        int databaseSizeBeforeUpdate = companionRepository.findAll().size();
        companion.setId(count.incrementAndGet());

        // Create the Companion
        CompanionDTO companionDTO = companionMapper.toDto(companion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(companionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Companion in the database
        List<Companion> companionList = companionRepository.findAll();
        assertThat(companionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompanion() throws Exception {
        int databaseSizeBeforeUpdate = companionRepository.findAll().size();
        companion.setId(count.incrementAndGet());

        // Create the Companion
        CompanionDTO companionDTO = companionMapper.toDto(companion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(companionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Companion in the database
        List<Companion> companionList = companionRepository.findAll();
        assertThat(companionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompanion() throws Exception {
        // Initialize the database
        companionRepository.saveAndFlush(companion);

        int databaseSizeBeforeDelete = companionRepository.findAll().size();

        // Delete the companion
        restCompanionMockMvc
            .perform(delete(ENTITY_API_URL_ID, companion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Companion> companionList = companionRepository.findAll();
        assertThat(companionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
