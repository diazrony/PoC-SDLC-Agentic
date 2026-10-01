package com.example.healthclinic.domain.model;

import com.example.monorepo.core.util.Preconditions;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Agregado de dominio.
 *
 * <p>Java puro: ni anotaciones JPA, ni Jackson, ni CDI. Esta clase se puede
 * instanciar y testear sin levantar absolutamente nada. Su equivalente
 * persistente es {@code infrastructure.persistence.entity.PatientEntity},
 * y la conversion entre ambos vive en un mapper explicito.</p>
 */
public final class Patient {

    private final PatientId id;
    private final String label;
    private final PatientStatus status;
    private final BigDecimal copaymentAmount;
    private final Instant createdAt;

    public Patient(PatientId id,
                      String label,
                      PatientStatus status,
                      BigDecimal copaymentAmount,
                      Instant createdAt) {
        this.id = Preconditions.requireNonNull(id, "id");
        this.label = Preconditions.requireNonBlank(label, "label");
        this.status = Preconditions.requireNonNull(status, "status");
        this.copaymentAmount = Preconditions.requireNonNull(copaymentAmount, "copaymentAmount");
        this.createdAt = Preconditions.requireNonNull(createdAt, "createdAt");
    }

    public PatientId id() {
        return id;
    }

    public String label() {
        return label;
    }

    public PatientStatus status() {
        return status;
    }

    public BigDecimal copaymentAmount() {
        return copaymentAmount;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public boolean isBlocked() {
        return status == PatientStatus.BLOCKED;
    }
}
