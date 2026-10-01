package com.example.transaction.domain.model;

import com.example.monorepo.core.util.Preconditions;

/**
 * Identificador del agregado como Value Object.
 *
 * <p>Evita el "obsesion por los tipos primitivos": un String suelto puede ser
 * cualquier cosa, un TransactionId solo puede ser esto.</p>
 */
public record TransactionId(String value) {

    public TransactionId {
        Preconditions.requireNonBlank(value, "TransactionId");
    }

    public static TransactionId of(String value) {
        return new TransactionId(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
