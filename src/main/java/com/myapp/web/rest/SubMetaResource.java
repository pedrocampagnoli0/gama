package com.myapp.web.rest;

import com.myapp.repository.SubMetaRepository;
import com.myapp.service.SubMetaService;
import com.myapp.service.dto.MetaDTO;
import com.myapp.service.dto.SubMetaDTO;
import com.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.myapp.domain.SubMeta}.
 */
@RestController
@RequestMapping("/api/sub-metas")
public class SubMetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(SubMetaResource.class);

    private static final String ENTITY_NAME = "subMeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubMetaService subMetaService;

    private final SubMetaRepository subMetaRepository;

    public SubMetaResource(SubMetaService subMetaService, SubMetaRepository subMetaRepository) {
        this.subMetaService = subMetaService;
        this.subMetaRepository = subMetaRepository;
    }

    /**
     * {@code POST  /sub-metas} : Create a new subMeta.
     *
     * @param subMetaDTO the subMetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subMetaDTO, or with status {@code 400 (Bad Request)} if the subMeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SubMetaDTO> createSubMeta(@Valid @RequestBody SubMetaDTO subMetaDTO) throws URISyntaxException {
        LOG.debug("REST request to save SubMeta : {}", subMetaDTO);
        if (subMetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new subMeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        subMetaDTO = subMetaService.save(subMetaDTO);
        return ResponseEntity.created(new URI("/api/sub-metas/" + subMetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, subMetaDTO.getId().toString()))
            .body(subMetaDTO);
    }

    /**
     * {@code PUT  /sub-metas/:id} : Updates an existing subMeta.
     *
     * @param id the id of the subMetaDTO to save.
     * @param subMetaDTO the subMetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subMetaDTO,
     * or with status {@code 400 (Bad Request)} if the subMetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subMetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubMetaDTO> updateSubMeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SubMetaDTO subMetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SubMeta : {}, {}", id, subMetaDTO);
        if (subMetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subMetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subMetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        subMetaDTO = subMetaService.update(subMetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subMetaDTO.getId().toString()))
            .body(subMetaDTO);
    }

    /**
     * {@code PATCH  /sub-metas/:id} : Partial updates given fields of an existing subMeta, field will ignore if it is null
     *
     * @param id the id of the subMetaDTO to save.
     * @param subMetaDTO the subMetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subMetaDTO,
     * or with status {@code 400 (Bad Request)} if the subMetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subMetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subMetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubMetaDTO> partialUpdateSubMeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SubMetaDTO subMetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SubMeta partially : {}, {}", id, subMetaDTO);
        if (subMetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subMetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subMetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubMetaDTO> result = subMetaService.partialUpdate(subMetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subMetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sub-metas} : get all the subMetas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subMetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SubMetaDTO>> getAllSubMetas(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of SubMetas");
        Page<SubMetaDTO> page = subMetaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sub-metas/:id} : get the "id" subMeta.
     *
     * @param id the id of the subMetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subMetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubMetaDTO> getSubMeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SubMeta : {}", id);
        Optional<SubMetaDTO> subMetaDTO = subMetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subMetaDTO);
    }

    /**
     * {@code DELETE  /sub-metas/:id} : delete the "id" subMeta.
     *
     * @param id the id of the subMetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubMeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SubMeta : {}", id);
        subMetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/by-meta/{metaId}")
    public ResponseEntity<List<SubMetaDTO>> getSubMetasByMetaId(@PathVariable Long metaId) {
        LOG.debug("REST request to get SubMetas for Meta ID: {}", metaId);
        List<SubMetaDTO> subMetas = subMetaService.findByMetaId(metaId);
        return ResponseEntity.ok(subMetas);
    }
}
