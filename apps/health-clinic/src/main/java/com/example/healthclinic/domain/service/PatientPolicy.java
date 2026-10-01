package com.example.healthclinic.domain.service;

import com.example.healthclinic.domain.exception.PatientBlockedException;
import com.example.healthclinic.domain.model.Patient;
import com.example.monorepo.exceptions.ValidationException;

import java.math.BigDecimal;

/**
 * Servicio de dominio: reglas que no pertenecen a un unico agregado.
 *
 * <p>Sin anotaciones. Se publica como bean CDI desde
 * {@code infrastructure.config}, de forma que el dominio permanece puro.</p>
 */
public class PatientPolicy {

    /** Un agregado bloqueado no se puede entregar al exterior. */
    public void ensureUsable(Patient aggregate) {
        if (aggregate.isBlocked()) {
            throw new PatientBlockedException(aggregate.id());
        }
    }

    /** Invariantes de creacion. */
    public void validateForCreation(String label, BigDecimal copaymentAmount) {
        if (label == null || label.isBlank()) {
            throw ValidationException.ofField("label", "es obligatorio");
        }
        if (copaymentAmount == null || copaymentAmount.signum() < 0) {
            throw ValidationException.ofField("copaymentAmount", "no puede ser negativo");
        }
    }
}
