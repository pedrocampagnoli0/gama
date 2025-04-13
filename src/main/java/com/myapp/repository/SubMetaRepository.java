package com.myapp.repository;

import com.myapp.domain.Meta;
import com.myapp.domain.SubMeta;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubMeta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubMetaRepository extends JpaRepository<SubMeta, Long> {
    // Find all SubMetas by Meta ID
    List<SubMeta> findByMetaId(Long metaId);

    // Delete all SubMetas by Meta ID
    void deleteByMetaId(Long metaId);
}
