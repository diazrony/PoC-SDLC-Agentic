package com.example.authorization.domain.service;

import com.example.authorization.domain.exception.AuthorizationBlockedException;
import com.example.authorization.domain.model.Authorization;
import com.example.monorepo.exceptions.ValidationException;

import java.math.BigDecimal;

/**
 * Servicio de dominio: reglas que no pertenecen a un unico agregado.
 *
 * <p>Sin anotaciones. Se publica como bean CDI desde
 * {@code infrastructure.config}, de forma que el dominio permanece puro.</p>
 */
public class AuthorizationPolicy {

    /** Un agregado bloqueado no se puede entregar al exterior. */
    public void ensureUsable(Authorization aggregate) {
        if (aggregate.isBlocked()) {
            throw new AuthorizationBlockedException(aggregate.id());
        }
    }

    /** Invariantes de creacion. */
    public void validateForCreation(String label, BigDecimal limitAmount) {
        if (label == null || label.isBlank()) {
            throw ValidationException.ofField("label", "es obligatorio");
        }
        if (limitAmount == null || limitAmount.signum() < 0) {
            throw ValidationException.ofField("limitAmount", "no puede ser negativo");
        }
    }
}
