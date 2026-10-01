package com.example.retiros.domain.model;

import com.example.monorepo.core.util.Preconditions;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Agregado de dominio.
 *
 * <p>Java puro: ni anotaciones JPA, ni Jackson, ni CDI. Esta clase se puede
 * instanciar y testear sin levantar absolutamente nada. Su equivalente
 * persistente es {@code infrastructure.persistence.entity.WithdrawalEntity},
 * y la conversion entre ambos vive en un mapper explicito.</p>
 */
public final class Withdrawal {

    private final WithdrawalId id;
    private final String label;
    private final WithdrawalStatus status;
    private final BigDecimal amount;
    private final Instant createdAt;

    public Withdrawal(WithdrawalId id,
                      String label,
                      WithdrawalStatus status,
                      BigDecimal amount,
                      Instant createdAt) {
        this.id = Preconditions.requireNonNull(id, "id");
        this.label = Preconditions.requireNonBlank(label, "label");
        this.status = Preconditions.requireNonNull(status, "status");
        this.amount = Preconditions.requireNonNull(amount, "amount");
        this.createdAt = Preconditions.requireNonNull(createdAt, "createdAt");
    }

    public WithdrawalId id() {
        return id;
    }

    public String label() {
        return label;
    }

    public WithdrawalStatus status() {
        return status;
    }

    public BigDecimal amount() {
        return amount;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public boolean isBlocked() {
        return status == WithdrawalStatus.BLOCKED;
    }
}
