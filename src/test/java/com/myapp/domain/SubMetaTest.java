package com.myapp.domain;

import static com.myapp.domain.MetaTestSamples.*;
import static com.myapp.domain.SubMetaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubMetaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubMeta.class);
        SubMeta subMeta1 = getSubMetaSample1();
        SubMeta subMeta2 = new SubMeta();
        assertThat(subMeta1).isNotEqualTo(subMeta2);

        subMeta2.setId(subMeta1.getId());
        assertThat(subMeta1).isEqualTo(subMeta2);

        subMeta2 = getSubMetaSample2();
        assertThat(subMeta1).isNotEqualTo(subMeta2);
    }

    @Test
    void metaTest() {
        SubMeta subMeta = getSubMetaRandomSampleGenerator();
        Meta metaBack = getMetaRandomSampleGenerator();

        subMeta.setMeta(metaBack);
        assertThat(subMeta.getMeta()).isEqualTo(metaBack);

        subMeta.meta(null);
        assertThat(subMeta.getMeta()).isNull();
    }
}
