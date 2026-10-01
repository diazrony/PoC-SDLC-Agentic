package com.example.authorization.domain.model;

import com.example.monorepo.core.util.Preconditions;

/**
 * Identificador del agregado como Value Object.
 *
 * <p>Evita el "obsesion por los tipos primitivos": un String suelto puede ser
 * cualquier cosa, un AuthorizationId solo puede ser esto.</p>
 */
public record AuthorizationId(String value) {

    public AuthorizationId {
        Preconditions.requireNonBlank(value, "AuthorizationId");
    }

    public static AuthorizationId of(String value) {
        return new AuthorizationId(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
