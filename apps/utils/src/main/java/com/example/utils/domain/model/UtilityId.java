package com.example.utils.domain.model;

import com.example.monorepo.core.util.Preconditions;

/**
 * Identificador del agregado como Value Object.
 *
 * <p>Evita el "obsesion por los tipos primitivos": un String suelto puede ser
 * cualquier cosa, un UtilityId solo puede ser esto.</p>
 */
public record UtilityId(String value) {

    public UtilityId {
        Preconditions.requireNonBlank(value, "UtilityId");
    }

    public static UtilityId of(String value) {
        return new UtilityId(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
