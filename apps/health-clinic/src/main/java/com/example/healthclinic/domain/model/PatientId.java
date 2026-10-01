package com.example.healthclinic.domain.model;

import com.example.monorepo.core.util.Preconditions;

/**
 * Identificador del agregado como Value Object.
 *
 * <p>Evita el "obsesion por los tipos primitivos": un String suelto puede ser
 * cualquier cosa, un PatientId solo puede ser esto.</p>
 */
public record PatientId(String value) {

    public PatientId {
        Preconditions.requireNonBlank(value, "PatientId");
    }

    public static PatientId of(String value) {
        return new PatientId(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
