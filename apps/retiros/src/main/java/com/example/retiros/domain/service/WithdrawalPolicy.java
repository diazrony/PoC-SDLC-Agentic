package com.example.retiros.domain.service;

import com.example.retiros.domain.exception.WithdrawalBlockedException;
import com.example.retiros.domain.model.Withdrawal;
import com.example.monorepo.exceptions.ValidationException;

import java.math.BigDecimal;

/**
 * Servicio de dominio: reglas que no pertenecen a un unico agregado.
 *
 * <p>Sin anotaciones. Se publica como bean CDI desde
 * {@code infrastructure.config}, de forma que el dominio permanece puro.</p>
 */
public class WithdrawalPolicy {

    /** Un agregado bloqueado no se puede entregar al exterior. */
    public void ensureUsable(Withdrawal aggregate) {
        if (aggregate.isBlocked()) {
            throw new WithdrawalBlockedException(aggregate.id());
        }
    }

    /** Invariantes de creacion. */
    public void validateForCreation(String label, BigDecimal amount) {
        if (label == null || label.isBlank()) {
            throw ValidationException.ofField("label", "es obligatorio");
        }
        if (amount == null || amount.signum() < 0) {
            throw ValidationException.ofField("amount", "no puede ser negativo");
        }
    }
}
