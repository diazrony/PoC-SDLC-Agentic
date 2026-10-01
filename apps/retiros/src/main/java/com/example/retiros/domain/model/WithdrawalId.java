package com.example.retiros.domain.model;

import com.example.monorepo.core.util.Preconditions;

/**
 * Identificador del agregado como Value Object.
 *
 * <p>Evita el "obsesion por los tipos primitivos": un String suelto puede ser
 * cualquier cosa, un WithdrawalId solo puede ser esto.</p>
 */
public record WithdrawalId(String value) {

    public WithdrawalId {
        Preconditions.requireNonBlank(value, "WithdrawalId");
    }

    public static WithdrawalId of(String value) {
        return new WithdrawalId(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
