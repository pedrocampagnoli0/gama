package com.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SubMetaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SubMeta getSubMetaSample1() {
        return new SubMeta().id(1L).descricao("descricao1");
    }

    public static SubMeta getSubMetaSample2() {
        return new SubMeta().id(2L).descricao("descricao2");
    }

    public static SubMeta getSubMetaRandomSampleGenerator() {
        return new SubMeta().id(longCount.incrementAndGet()).descricao(UUID.randomUUID().toString());
    }
}
