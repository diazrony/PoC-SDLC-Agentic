package com.example.monorepo.core;

import com.example.monorepo.core.util.Preconditions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PreconditionsTest {

    @Test
    void requireNonBlankDevuelveElValorCuandoEsValido() {
        assertEquals("ok", Preconditions.requireNonBlank("ok", "campo"));
    }

    @Test
    void requireNonBlankFallaConCadenaVacia() {
        assertThrows(IllegalArgumentException.class,
                () -> Preconditions.requireNonBlank("  ", "campo"));
    }
}
