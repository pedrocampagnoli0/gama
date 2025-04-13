package com.myapp.service.mapper;

import com.myapp.domain.Meta;
import com.myapp.domain.SubMeta;
import com.myapp.service.dto.MetaDTO;
import com.myapp.service.dto.SubMetaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubMeta} and its DTO {@link SubMetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubMetaMapper extends EntityMapper<SubMetaDTO, SubMeta> {
    @Mapping(target = "meta", source = "meta", qualifiedByName = "metaId")
    SubMetaDTO toDto(SubMeta s);

    @Named("metaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MetaDTO toDtoMetaId(Meta meta);
}
