package com.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubMetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubMetaDTO.class);
        SubMetaDTO subMetaDTO1 = new SubMetaDTO();
        subMetaDTO1.setId(1L);
        SubMetaDTO subMetaDTO2 = new SubMetaDTO();
        assertThat(subMetaDTO1).isNotEqualTo(subMetaDTO2);
        subMetaDTO2.setId(subMetaDTO1.getId());
        assertThat(subMetaDTO1).isEqualTo(subMetaDTO2);
        subMetaDTO2.setId(2L);
        assertThat(subMetaDTO1).isNotEqualTo(subMetaDTO2);
        subMetaDTO1.setId(null);
        assertThat(subMetaDTO1).isNotEqualTo(subMetaDTO2);
    }
}
