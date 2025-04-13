package com.myapp.service;

import com.myapp.domain.SubMeta;
import com.myapp.repository.SubMetaRepository;
import com.myapp.service.dto.MetaDTO;
import com.myapp.service.dto.SubMetaDTO;
import com.myapp.service.mapper.SubMetaMapper;
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
 * Service Implementation for managing {@link com.myapp.domain.SubMeta}.
 */
@Service
@Transactional
public class SubMetaService {

    private static final Logger LOG = LoggerFactory.getLogger(SubMetaService.class);

    private final SubMetaRepository subMetaRepository;

    private final SubMetaMapper subMetaMapper;

    public SubMetaService(SubMetaRepository subMetaRepository, SubMetaMapper subMetaMapper) {
        this.subMetaRepository = subMetaRepository;
        this.subMetaMapper = subMetaMapper;
    }

    /**
     * Save a subMeta.
     *
     * @param subMetaDTO the entity to save.
     * @return the persisted entity.
     */
    public SubMetaDTO save(SubMetaDTO subMetaDTO) {
        LOG.debug("Request to save SubMeta : {}", subMetaDTO);
        SubMeta subMeta = subMetaMapper.toEntity(subMetaDTO);
        subMeta = subMetaRepository.save(subMeta);
        return subMetaMapper.toDto(subMeta);
    }

    /**
     * Update a subMeta.
     *
     * @param subMetaDTO the entity to save.
     * @return the persisted entity.
     */
    public SubMetaDTO update(SubMetaDTO subMetaDTO) {
        LOG.debug("Request to update SubMeta : {}", subMetaDTO);
        SubMeta subMeta = subMetaMapper.toEntity(subMetaDTO);
        subMeta = subMetaRepository.save(subMeta);
        return subMetaMapper.toDto(subMeta);
    }

    /**
     * returns the submetas linked to a metaID.
     *
     * @param metaId the metas to find the submetas.
     * @return the submetas linked to this meta
     */
    @Transactional(readOnly = true)
    public List<SubMetaDTO> findByMetaId(Long metaId) {
        LOG.debug("Request to get subMeta for Meta ID: {}", metaId);
        return subMetaRepository.findByMetaId(metaId).stream().map(subMetaMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Partially update a subMeta.
     *
     * @param subMetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SubMetaDTO> partialUpdate(SubMetaDTO subMetaDTO) {
        LOG.debug("Request to partially update SubMeta : {}", subMetaDTO);

        return subMetaRepository
            .findById(subMetaDTO.getId())
            .map(existingSubMeta -> {
                subMetaMapper.partialUpdate(existingSubMeta, subMetaDTO);

                return existingSubMeta;
            })
            .map(subMetaRepository::save)
            .map(subMetaMapper::toDto);
    }

    /**
     * Get all the subMetas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SubMetaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SubMetas");
        return subMetaRepository.findAll(pageable).map(subMetaMapper::toDto);
    }

    /**
     * Get one subMeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SubMetaDTO> findOne(Long id) {
        LOG.debug("Request to get SubMeta : {}", id);
        return subMetaRepository.findById(id).map(subMetaMapper::toDto);
    }

    /**
     * Delete the subMeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SubMeta : {}", id);
        subMetaRepository.deleteById(id);
    }
}
