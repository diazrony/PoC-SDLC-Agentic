package com.example.transaction.domain.service;

import com.example.transaction.domain.exception.TransactionBlockedException;
import com.example.transaction.domain.model.Transaction;
import com.example.monorepo.exceptions.ValidationException;

import java.math.BigDecimal;

/**
 * Servicio de dominio: reglas que no pertenecen a un unico agregado.
 *
 * <p>Sin anotaciones. Se publica como bean CDI desde
 * {@code infrastructure.config}, de forma que el dominio permanece puro.</p>
 */
public class TransactionPolicy {

    /** Un agregado bloqueado no se puede entregar al exterior. */
    public void ensureUsable(Transaction aggregate) {
        if (aggregate.isBlocked()) {
            throw new TransactionBlockedException(aggregate.id());
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
