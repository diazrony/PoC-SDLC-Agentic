package com.example.monorepo.testing.fixture;

import com.example.monorepo.core.util.Ids;

import java.math.BigDecimal;
import java.time.Instant;

/** Valores deterministas para pruebas. */
public final class Fixtures {

    /** Instante fijo para que las aserciones no dependan del reloj. */
    public static final Instant FIXED_INSTANT = Instant.parse("2026-01-01T00:00:00Z");

    private Fixtures() {
    }

    public static String anyId() {
        return Ids.newId();
    }

    public static String idOf(String prefix, int sequence) {
        return "%s-%04d".formatted(prefix, sequence);
    }

    public static BigDecimal amount(String value) {
        return new BigDecimal(value);
    }
}
