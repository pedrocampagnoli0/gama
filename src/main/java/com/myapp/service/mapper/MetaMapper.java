package com.myapp.service.mapper;

import com.myapp.domain.Aluno;
import com.myapp.domain.Meta;
import com.myapp.service.dto.AlunoDTO;
import com.myapp.service.dto.MetaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Meta} and its DTO {@link MetaDTO}.
 */
@Mapper(componentModel = "spring")
public interface MetaMapper extends EntityMapper<MetaDTO, Meta> {
    @Mapping(target = "aluno", source = "aluno", qualifiedByName = "alunoMatricula")
    MetaDTO toDto(Meta s);

    @Named("alunoMatricula")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "matricula", source = "matricula")
    AlunoDTO toDtoAlunoMatricula(Aluno aluno);
}
