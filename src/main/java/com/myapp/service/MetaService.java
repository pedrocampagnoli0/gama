package com.myapp.service;

import com.myapp.domain.Meta;
import com.myapp.repository.MetaRepository;
import com.myapp.repository.SubMetaRepository;
import com.myapp.service.dto.MetaDTO;
import com.myapp.service.mapper.MetaMapper;
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
 * Service Implementation for managing {@link Meta}.
 */
@Service
@Transactional
public class MetaService {

    private static final Logger LOG = LoggerFactory.getLogger(MetaService.class);

    private final MetaRepository metaRepository;
    private final SubMetaRepository subMetaRepository;

    private final MetaMapper metaMapper;

    public MetaService(MetaRepository metaRepository, SubMetaRepository subMetaRepository, MetaMapper metaMapper) {
        this.metaRepository = metaRepository;
        this.subMetaRepository = subMetaRepository;
        this.metaMapper = metaMapper;
    }

    /**
     * Save a meta.
     *
     * @param metaDTO the entity to save.
     * @return the persisted entity.
     */
    public MetaDTO save(MetaDTO metaDTO) {
        LOG.debug("Request to save Meta : {}", metaDTO);
        Meta meta = metaMapper.toEntity(metaDTO);
        if (metaRepository.existsByAlunoAndArea(meta.getAluno(), meta.getArea())) {
            throw new IllegalArgumentException("A goal already exists for this student in this area.");
        }
        meta = metaRepository.save(meta);
        return metaMapper.toDto(meta);
    }

    @Transactional(readOnly = true)
    public List<MetaDTO> findByAlunoId(Long alunoId) {
        LOG.debug("Request to get Metas for Aluno ID: {}", alunoId);
        return metaRepository.findByAlunoId(alunoId).stream().map(metaMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Update a meta.
     *
     * @param metaDTO the entity to save.
     * @return the persisted entity.
     */
    public MetaDTO update(MetaDTO metaDTO) {
        LOG.debug("Request to update Meta : {}", metaDTO);
        Meta meta = metaMapper.toEntity(metaDTO);
        if (metaRepository.existsByAlunoAndAreaAndIdNot(meta.getAluno(), meta.getArea(), meta.getId())) {
            throw new IllegalArgumentException("A goal already exists for this student in this area.");
        }
        meta = metaRepository.save(meta);
        return metaMapper.toDto(meta);
    }

    /**
     * Partially update a meta.
     *
     * @param metaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MetaDTO> partialUpdate(MetaDTO metaDTO) {
        LOG.debug("Request to partially update Meta : {}", metaDTO);

        return metaRepository
            .findById(metaDTO.getId())
            .map(existingMeta -> {
                metaMapper.partialUpdate(existingMeta, metaDTO);
                if (metaRepository.existsByAlunoAndAreaAndIdNot(existingMeta.getAluno(), existingMeta.getArea(), existingMeta.getId())) {
                    throw new IllegalArgumentException("A goal already exists for this student in this area.");
                }
                return existingMeta;
            })
            .map(metaRepository::save)
            .map(metaMapper::toDto);
    }

    /**
     * Get all the metas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MetaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Metas");
        return metaRepository.findAll(pageable).map(metaMapper::toDto);
    }

    /**
     * Get all the metas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MetaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return metaRepository.findAllWithEagerRelationships(pageable).map(metaMapper::toDto);
    }

    /**
     * Get one meta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MetaDTO> findOne(Long id) {
        LOG.debug("Request to get Meta : {}", id);
        return metaRepository.findOneWithEagerRelationships(id).map(metaMapper::toDto);
    }

    /**
     * Delete the meta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Meta : {}", id);
        subMetaRepository.deleteByMetaId(id);
        metaRepository.deleteById(id);
    }
}
