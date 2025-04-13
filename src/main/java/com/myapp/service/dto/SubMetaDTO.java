package com.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.myapp.domain.SubMeta} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubMetaDTO implements Serializable {

    private Long id;

    @NotNull
    private String descricao;

    @NotNull
    private Boolean concluida;

    @NotNull
    private LocalDate dataLimite;

    private MetaDTO meta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getConcluida() {
        return concluida;
    }

    public void setConcluida(Boolean concluida) {
        this.concluida = concluida;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }

    public MetaDTO getMeta() {
        return meta;
    }

    public void setMeta(MetaDTO meta) {
        this.meta = meta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubMetaDTO)) {
            return false;
        }

        SubMetaDTO subMetaDTO = (SubMetaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subMetaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubMetaDTO{" +
            "id=" + getId() +
            ", descricao='" + getDescricao() + "'" +
            ", concluida='" + getConcluida() + "'" +
            ", dataLimite='" + getDataLimite() + "'" +
            ", meta=" + getMeta() +
            "}";
    }
}
