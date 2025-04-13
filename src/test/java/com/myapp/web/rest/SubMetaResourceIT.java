package com.myapp.web.rest;

import static com.myapp.domain.SubMetaAsserts.*;
import static com.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.IntegrationTest;
import com.myapp.domain.SubMeta;
import com.myapp.repository.SubMetaRepository;
import com.myapp.service.dto.SubMetaDTO;
import com.myapp.service.mapper.SubMetaMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SubMetaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubMetaResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CONCLUIDA = false;
    private static final Boolean UPDATED_CONCLUIDA = true;

    private static final LocalDate DEFAULT_DATA_LIMITE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_LIMITE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/sub-metas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubMetaRepository subMetaRepository;

    @Autowired
    private SubMetaMapper subMetaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubMetaMockMvc;

    private SubMeta subMeta;

    private SubMeta insertedSubMeta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubMeta createEntity() {
        return new SubMeta().descricao(DEFAULT_DESCRICAO).concluida(DEFAULT_CONCLUIDA).dataLimite(DEFAULT_DATA_LIMITE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubMeta createUpdatedEntity() {
        return new SubMeta().descricao(UPDATED_DESCRICAO).concluida(UPDATED_CONCLUIDA).dataLimite(UPDATED_DATA_LIMITE);
    }

    @BeforeEach
    void initTest() {
        subMeta = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSubMeta != null) {
            subMetaRepository.delete(insertedSubMeta);
            insertedSubMeta = null;
        }
    }

    @Test
    @Transactional
    void createSubMeta() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SubMeta
        SubMetaDTO subMetaDTO = subMetaMapper.toDto(subMeta);
        var returnedSubMetaDTO = om.readValue(
            restSubMetaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subMetaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SubMetaDTO.class
        );

        // Validate the SubMeta in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSubMeta = subMetaMapper.toEntity(returnedSubMetaDTO);
        assertSubMetaUpdatableFieldsEquals(returnedSubMeta, getPersistedSubMeta(returnedSubMeta));

        insertedSubMeta = returnedSubMeta;
    }

    @Test
    @Transactional
    void createSubMetaWithExistingId() throws Exception {
        // Create the SubMeta with an existing ID
        subMeta.setId(1L);
        SubMetaDTO subMetaDTO = subMetaMapper.toDto(subMeta);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubMetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subMetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SubMeta in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescricaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subMeta.setDescricao(null);

        // Create the SubMeta, which fails.
        SubMetaDTO subMetaDTO = subMetaMapper.toDto(subMeta);

        restSubMetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subMetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConcluidaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subMeta.setConcluida(null);

        // Create the SubMeta, which fails.
        SubMetaDTO subMetaDTO = subMetaMapper.toDto(subMeta);

        restSubMetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subMetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataLimiteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subMeta.setDataLimite(null);

        // Create the SubMeta, which fails.
        SubMetaDTO subMetaDTO = subMetaMapper.toDto(subMeta);

        restSubMetaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subMetaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubMetas() throws Exception {
        // Initialize the database
        insertedSubMeta = subMetaRepository.saveAndFlush(subMeta);

        // Get all the subMetaList
        restSubMetaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subMeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].concluida").value(hasItem(DEFAULT_CONCLUIDA)))
            .andExpect(jsonPath("$.[*].dataLimite").value(hasItem(DEFAULT_DATA_LIMITE.toString())));
    }

    @Test
    @Transactional
    void getSubMeta() throws Exception {
        // Initialize the database
        insertedSubMeta = subMetaRepository.saveAndFlush(subMeta);

        // Get the subMeta
        restSubMetaMockMvc
            .perform(get(ENTITY_API_URL_ID, subMeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subMeta.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.concluida").value(DEFAULT_CONCLUIDA))
            .andExpect(jsonPath("$.dataLimite").value(DEFAULT_DATA_LIMITE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSubMeta() throws Exception {
        // Get the subMeta
        restSubMetaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubMeta() throws Exception {
        // Initialize the database
        insertedSubMeta = subMetaRepository.saveAndFlush(subMeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subMeta
        SubMeta updatedSubMeta = subMetaRepository.findById(subMeta.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubMeta are not directly saved in db
        em.detach(updatedSubMeta);
        updatedSubMeta.descricao(UPDATED_DESCRICAO).concluida(UPDATED_CONCLUIDA).dataLimite(UPDATED_DATA_LIMITE);
        SubMetaDTO subMetaDTO = subMetaMapper.toDto(updatedSubMeta);

        restSubMetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subMetaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subMetaDTO))
            )
            .andExpect(status().isOk());

        // Validate the SubMeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubMetaToMatchAllProperties(updatedSubMeta);
    }

    @Test
    @Transactional
    void putNonExistingSubMeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subMeta.setId(longCount.incrementAndGet());

        // Create the SubMeta
        SubMetaDTO subMetaDTO = subMetaMapper.toDto(subMeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubMetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subMetaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subMetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubMeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubMeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subMeta.setId(longCount.incrementAndGet());

        // Create the SubMeta
        SubMetaDTO subMetaDTO = subMetaMapper.toDto(subMeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubMetaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subMetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubMeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubMeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subMeta.setId(longCount.incrementAndGet());

        // Create the SubMeta
        SubMetaDTO subMetaDTO = subMetaMapper.toDto(subMeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubMetaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subMetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubMeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubMetaWithPatch() throws Exception {
        // Initialize the database
        insertedSubMeta = subMetaRepository.saveAndFlush(subMeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subMeta using partial update
        SubMeta partialUpdatedSubMeta = new SubMeta();
        partialUpdatedSubMeta.setId(subMeta.getId());

        partialUpdatedSubMeta.concluida(UPDATED_CONCLUIDA);

        restSubMetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubMeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubMeta))
            )
            .andExpect(status().isOk());

        // Validate the SubMeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubMetaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSubMeta, subMeta), getPersistedSubMeta(subMeta));
    }

    @Test
    @Transactional
    void fullUpdateSubMetaWithPatch() throws Exception {
        // Initialize the database
        insertedSubMeta = subMetaRepository.saveAndFlush(subMeta);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subMeta using partial update
        SubMeta partialUpdatedSubMeta = new SubMeta();
        partialUpdatedSubMeta.setId(subMeta.getId());

        partialUpdatedSubMeta.descricao(UPDATED_DESCRICAO).concluida(UPDATED_CONCLUIDA).dataLimite(UPDATED_DATA_LIMITE);

        restSubMetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubMeta.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubMeta))
            )
            .andExpect(status().isOk());

        // Validate the SubMeta in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubMetaUpdatableFieldsEquals(partialUpdatedSubMeta, getPersistedSubMeta(partialUpdatedSubMeta));
    }

    @Test
    @Transactional
    void patchNonExistingSubMeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subMeta.setId(longCount.incrementAndGet());

        // Create the SubMeta
        SubMetaDTO subMetaDTO = subMetaMapper.toDto(subMeta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubMetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subMetaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subMetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubMeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubMeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subMeta.setId(longCount.incrementAndGet());

        // Create the SubMeta
        SubMetaDTO subMetaDTO = subMetaMapper.toDto(subMeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubMetaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subMetaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubMeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubMeta() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subMeta.setId(longCount.incrementAndGet());

        // Create the SubMeta
        SubMetaDTO subMetaDTO = subMetaMapper.toDto(subMeta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubMetaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(subMetaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubMeta in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubMeta() throws Exception {
        // Initialize the database
        insertedSubMeta = subMetaRepository.saveAndFlush(subMeta);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the subMeta
        restSubMetaMockMvc
            .perform(delete(ENTITY_API_URL_ID, subMeta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return subMetaRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected SubMeta getPersistedSubMeta(SubMeta subMeta) {
        return subMetaRepository.findById(subMeta.getId()).orElseThrow();
    }

    protected void assertPersistedSubMetaToMatchAllProperties(SubMeta expectedSubMeta) {
        assertSubMetaAllPropertiesEquals(expectedSubMeta, getPersistedSubMeta(expectedSubMeta));
    }

    protected void assertPersistedSubMetaToMatchUpdatableProperties(SubMeta expectedSubMeta) {
        assertSubMetaAllUpdatablePropertiesEquals(expectedSubMeta, getPersistedSubMeta(expectedSubMeta));
    }
}
