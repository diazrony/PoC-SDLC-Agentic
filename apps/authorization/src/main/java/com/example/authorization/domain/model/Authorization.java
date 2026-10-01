package com.example.authorization.domain.model;

import com.example.monorepo.core.util.Preconditions;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Agregado de dominio.
 *
 * <p>Java puro: ni anotaciones JPA, ni Jackson, ni CDI. Esta clase se puede
 * instanciar y testear sin levantar absolutamente nada. Su equivalente
 * persistente es {@code infrastructure.persistence.entity.AuthorizationEntity},
 * y la conversion entre ambos vive en un mapper explicito.</p>
 */
public final class Authorization {

    private final AuthorizationId id;
    private final String label;
    private final AuthorizationStatus status;
    private final BigDecimal limitAmount;
    private final Instant createdAt;

    public Authorization(AuthorizationId id,
                      String label,
                      AuthorizationStatus status,
                      BigDecimal limitAmount,
                      Instant createdAt) {
        this.id = Preconditions.requireNonNull(id, "id");
        this.label = Preconditions.requireNonBlank(label, "label");
        this.status = Preconditions.requireNonNull(status, "status");
        this.limitAmount = Preconditions.requireNonNull(limitAmount, "limitAmount");
        this.createdAt = Preconditions.requireNonNull(createdAt, "createdAt");
    }

    public AuthorizationId id() {
        return id;
    }

    public String label() {
        return label;
    }

    public AuthorizationStatus status() {
        return status;
    }

    public BigDecimal limitAmount() {
        return limitAmount;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public boolean isBlocked() {
        return status == AuthorizationStatus.BLOCKED;
    }
}
