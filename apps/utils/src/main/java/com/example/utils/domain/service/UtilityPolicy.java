package com.example.utils.domain.service;

import com.example.utils.domain.exception.UtilityBlockedException;
import com.example.utils.domain.model.Utility;
import com.example.monorepo.exceptions.ValidationException;

import java.math.BigDecimal;

/**
 * Servicio de dominio: reglas que no pertenecen a un unico agregado.
 *
 * <p>Sin anotaciones. Se publica como bean CDI desde
 * {@code infrastructure.config}, de forma que el dominio permanece puro.</p>
 */
public class UtilityPolicy {

    /** Un agregado bloqueado no se puede entregar al exterior. */
    public void ensureUsable(Utility aggregate) {
        if (aggregate.isBlocked()) {
            throw new UtilityBlockedException(aggregate.id());
        }
    }

    /** Invariantes de creacion. */
    public void validateForCreation(String label, BigDecimal threshold) {
        if (label == null || label.isBlank()) {
            throw ValidationException.ofField("label", "es obligatorio");
        }
        if (threshold == null || threshold.signum() < 0) {
            throw ValidationException.ofField("threshold", "no puede ser negativo");
        }
    }
}
