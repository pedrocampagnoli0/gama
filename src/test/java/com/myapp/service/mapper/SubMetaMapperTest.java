package com.myapp.service.mapper;

import static com.myapp.domain.SubMetaAsserts.*;
import static com.myapp.domain.SubMetaTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SubMetaMapperTest {

    private SubMetaMapper subMetaMapper;

    @BeforeEach
    void setUp() {
        subMetaMapper = new SubMetaMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSubMetaSample1();
        var actual = subMetaMapper.toEntity(subMetaMapper.toDto(expected));
        assertSubMetaAllPropertiesEquals(expected, actual);
    }
}
