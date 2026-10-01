package com.example.utils.domain.model;

import com.example.monorepo.core.util.Preconditions;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Agregado de dominio.
 *
 * <p>Java puro: ni anotaciones JPA, ni Jackson, ni CDI. Esta clase se puede
 * instanciar y testear sin levantar absolutamente nada. Su equivalente
 * persistente es {@code infrastructure.persistence.entity.UtilityEntity},
 * y la conversion entre ambos vive en un mapper explicito.</p>
 */
public final class Utility {

    private final UtilityId id;
    private final String label;
    private final UtilityStatus status;
    private final BigDecimal threshold;
    private final Instant createdAt;

    public Utility(UtilityId id,
                      String label,
                      UtilityStatus status,
                      BigDecimal threshold,
                      Instant createdAt) {
        this.id = Preconditions.requireNonNull(id, "id");
        this.label = Preconditions.requireNonBlank(label, "label");
        this.status = Preconditions.requireNonNull(status, "status");
        this.threshold = Preconditions.requireNonNull(threshold, "threshold");
        this.createdAt = Preconditions.requireNonNull(createdAt, "createdAt");
    }

    public UtilityId id() {
        return id;
    }

    public String label() {
        return label;
    }

    public UtilityStatus status() {
        return status;
    }

    public BigDecimal threshold() {
        return threshold;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public boolean isBlocked() {
        return status == UtilityStatus.BLOCKED;
    }
}
